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
import android.widget.ImageView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.TareaProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;

public class EditTareaActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialogInicio;
    private DatePickerDialog datePickerDialogFin;
    String mExtraTareaId;
    long fecha_inicio = 0;
    long fecha_fin = 0;
    TareaProvider mTareaProvider;
    AlertDialog mDialog;

    Button btnFechaInicio;
    Button btnFechaFin;
    Button btnActualizar;
    TextInputEditText textViewTitle;
    TextInputEditText textViewDesc;
    TextInputEditText textViewRepositorio;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tarea);

        mExtraTareaId = getIntent().getStringExtra("idTarea");
        mTareaProvider = new TareaProvider();

        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFinal);
        btnActualizar = findViewById(R.id.btnActualizar);
        textViewTitle = findViewById(R.id.textInputNombreEvento);
        textViewDesc = findViewById(R.id.textInputDescripcion);
        textViewRepositorio = findViewById(R.id.textInputRepositorio);
        mImageViewBack = findViewById(R.id.circleImageBack);

        initDatePickerInicio();
        initDatePickerFin();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        getTarea();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
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
    }

    private void update() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date1 = (Date)formatter.parse(btnFechaInicio.getText().toString());
            Date date2 = (Date)formatter.parse(btnFechaFin.getText().toString());
            fecha_inicio = date1.getTime();
            fecha_fin = date2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Tarea tarea = new Tarea();
        tarea.setId(mExtraTareaId);
        tarea.setNombre(textViewTitle.getText().toString());
        tarea.setDescripcion(textViewDesc.getText().toString());
        tarea.setRepositorio(textViewRepositorio.getText().toString());
        tarea.setFecha_inicio(fecha_inicio);
        tarea.setFecha_fin(fecha_fin);
        updateInfo(tarea);
    }

    private void updateInfo(Tarea tarea) {
        mDialog.show();
        mTareaProvider.update(tarea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent miIntent = new Intent(EditTareaActivity.this, MenuActivity.class);
                    startActivity(miIntent);
                    Toast.makeText(EditTareaActivity.this, "La información se actualizo correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditTareaActivity.this, "La información no se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTarea() {
        mTareaProvider.getTareaById(mExtraTareaId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    if (documentSnapshot.contains("fecha_inicio")) {
                        long timestamp_inicio = documentSnapshot.getLong("fecha_inicio");
                        Date date1 = new Date(timestamp_inicio);
                        String fecha_inicio = formatter.format(date1);
                        btnFechaInicio.setText(fecha_inicio);
                    }
                    if (documentSnapshot.contains("fecha_fin")) {
                        long timestamp_fin = documentSnapshot.getLong("fecha_fin");
                        Date date2 = new Date(timestamp_fin);
                        String fecha_fin = formatter.format(date2);
                        btnFechaFin.setText(fecha_fin);
                    }
                    if (documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        textViewTitle.setText(nombre);
                    }
                    if (documentSnapshot.contains("descripcion")){
                        String descripcion = documentSnapshot.getString("descripcion");
                        textViewDesc.setText(descripcion);
                    }
                    if (documentSnapshot.contains("repositorio")){
                        String repositorio = documentSnapshot.getString("repositorio");
                        textViewRepositorio.setText(repositorio);
                    }
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