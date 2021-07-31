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
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class NuevoProyectoActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialogInicio;
    private DatePickerDialog datePickerDialogFin;
    long fecha_inicio;
    long fecha_fin;
    int randomNum = 0;
    boolean existe = false;
    String codigo = "";
    CircleImageView mCircleImageViewBack;
    TextView textViewTitle;
    Button btnFechaInicio;
    Button btnFechaFin;
    Button btnCrear;

    AlertDialog mDialog;

    AuthProvider mAuthProvider;
    ProyectoProvider mProyectoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_proyecto);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        textViewTitle = findViewById(R.id.textInputNombreProyecto);
        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFinal);
        btnCrear = findViewById(R.id.btnAceptar);
        initDatePickerInicio();
        initDatePickerFin();
        btnFechaInicio.setText(getTodaysDate());
        btnFechaFin.setText(getTodaysDate());

        mAuthProvider = new AuthProvider();
        mProyectoProvider = new ProyectoProvider();

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
                if (!textViewTitle.getText().toString().isEmpty()){
                    crearProyecto();
                }
                else{
                    Toast.makeText(NuevoProyectoActivity.this, "Nombre del proyecto es necesario", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void crearProyecto() {
        String idUsuario = mAuthProvider.getUid().toString();
        String nombre = textViewTitle.getText().toString();

        String[] usuarioArray = idUsuario.split("\\s*,\\s*");
        List<String> usuarios = Arrays.asList(usuarioArray);

        int min_val = 0;
        int max_val = 1000;
        Random ran = new Random();
        int randomNum = ran.nextInt(max_val) + min_val;
        codigo = nombre.replaceAll("\\s+","") + String.valueOf(randomNum);

        while (checkIfExists(codigo)){
            randomNum += 1;
            codigo = nombre.replaceAll("\\s+","") + String.valueOf(randomNum);
        }

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setFecha_inicio(fecha_inicio);
        proyecto.setFecha_fin(fecha_fin);
        proyecto.setEquipo(usuarios);
        proyecto.setCodigo(codigo);
        mDialog.show();
        mProyectoProvider.save(proyecto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    Intent intent = new Intent(NuevoProyectoActivity.this, MenuActivity.class);
                    startActivity(intent);
                    Toast.makeText(NuevoProyectoActivity.this, "La información se almacenó correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(NuevoProyectoActivity.this, "Hubo un error al amacenar", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean checkIfExists(String cod) {
        final boolean[] exists = {false};
        mProyectoProvider.getCodigo(cod).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    exists[0] = true;
                }
            }
        });
        return exists[0];
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePickerInicio()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnFechaInicio.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogInicio = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        fecha_inicio = cal.getTimeInMillis();
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
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogFin = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        fecha_fin = cal.getTimeInMillis();
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
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