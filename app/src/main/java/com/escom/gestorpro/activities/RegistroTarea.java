package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.TareaProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistroTarea extends AppCompatActivity {
    private DatePickerDialog datePickerDialogInicio;
    private DatePickerDialog datePickerDialogFin;
    long fecha_inicio = 0;
    long fecha_fin = 0;
    AlertDialog mDialog;
    CircleImageView mCircleImageViewBack;
    TextInputEditText mtextInputNombreEvento;
    TextInputEditText mtextInputDesc;
    TextInputEditText mtextInputRepositorio;
    Button btnFechaInicio;
    Button btnFechaFin;
    Button btnCrear;

    TareaProvider mTareaProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mtextInputNombreEvento = findViewById(R.id.textInputNombreEvento);
        mtextInputDesc = findViewById(R.id.textInputDescripcion);
        mtextInputRepositorio = findViewById(R.id.textInputRepositorio);
        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFinal);
        btnCrear = findViewById(R.id.btnAceptar);

        mTareaProvider = new TareaProvider();

        initDatePickerInicio();
        initDatePickerFin();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogInicio.show();
            }
        });

        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogFin.show();
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mtextInputNombreEvento.getText().toString().isEmpty() && fecha_inicio!= 0 && fecha_fin!= 0){
                    crearTarea();
                }
                else{
                    Toast.makeText(RegistroTarea.this, "Nombre del evento, fechas de inicio y final son necesarios", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void crearTarea() {
        String nombre = mtextInputNombreEvento.getText().toString();
        String descr = mtextInputDesc.getText().toString();
        String repo = mtextInputRepositorio.getText().toString();

        Tarea tarea = new Tarea();
        tarea.setNombre(nombre);
        tarea.setDescripcion(descr);
        tarea.setRepositorio(repo);
        tarea.setFecha_inicio(fecha_inicio);
        tarea.setFecha_fin(fecha_fin);
        mDialog.show();
        mTareaProvider.save(tarea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    Intent intent = new Intent(RegistroTarea.this, TareaDetailActivity.class);
                    //intent.putExtra("id", tarea.getId());
                    startActivity(intent);
                    Toast.makeText(RegistroTarea.this, "La información se almacenó correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(RegistroTarea.this, "Hubo un error al amacenar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initDatePickerInicio()
    {
        DatePickerDialog.OnDateSetListener dateSetListener_inicio = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnFechaInicio.setText(date);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    Date date2 = (Date)formatter.parse(date);
                    fecha_inicio = date2.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialogInicio = new DatePickerDialog(this, style, dateSetListener_inicio, year, month, day);
    }

    private void initDatePickerFin()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnFechaFin.setText(date);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    Date date2 = (Date)formatter.parse(date);
                    fecha_fin = date2.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialogFin = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)
    {
        return day + "-" + getMonthFormat(month) + "-" + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "ENE";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "ABR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AGO";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DIC";

        //default should never happen
        return "ENE";
    }
}