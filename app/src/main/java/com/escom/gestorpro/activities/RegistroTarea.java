package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistroTarea extends AppCompatActivity {
    private DatePickerDialog datePickerDialogInicio;
    private DatePickerDialog datePickerDialogFin;
    long fecha_inicio = 0;
    long fecha_fin = 0;
    String id_proyecto = "";
    String id_usuario = "";
    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();
    List<String> equipo = new ArrayList<>();
    List<String> id_equipo = new ArrayList<>();
    ArrayAdapter<String> adapter_equipo;
    ArrayAdapter<String> adapter;

    ProyectoProvider mProyectoProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    AlertDialog mDialog;
    CircleImageView mCircleImageViewBack;
    TextInputEditText mtextInputNombreEvento;
    TextInputEditText mtextInputDesc;
    TextInputEditText mtextInputRepositorio;
    Button btnFechaInicio;
    Button btnFechaFin;
    Button btnCrear;
    Spinner spinnerEquipo;
    Spinner spinnerProyecto;

    TareaProvider mTareaProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);

        mProyectoProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mtextInputNombreEvento = findViewById(R.id.textInputNombreEvento);
        mtextInputDesc = findViewById(R.id.textInputDescripcion);
        mtextInputRepositorio = findViewById(R.id.textInputRepositorio);
        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFinal);
        btnCrear = findViewById(R.id.btnAceptar);
        spinnerEquipo = (Spinner)findViewById(R.id.spinerEquipo);
        spinnerProyecto = (Spinner)findViewById(R.id.spinerProyecto);

        mTareaProvider = new TareaProvider();

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, proyectos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProyecto.setAdapter(adapter);

        adapter_equipo = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, equipo);
        adapter_equipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEquipo.setAdapter(adapter_equipo);

        initDatePickerInicio();
        initDatePickerFin();
        loadProyecto();

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

        spinnerProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_proyecto = id_proyectos.get(spinnerProyecto.getSelectedItemPosition());
                loadEquipo(id_proyecto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerEquipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_usuario = id_equipo.get(spinnerEquipo.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadProyecto() {
        mProyectoProvider.getProyectoByUser2(mAuthProvider.getUid()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nombre_proyectos = document.getString("nombre");
                        String id = document.getString("id");
                        proyectos.add(nombre_proyectos);
                        id_proyectos.add(id);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadEquipo(String id) {
        mProyectoProvider.getProyectoById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot_proyecto) {
                if (documentSnapshot_proyecto.exists()){
                    ArrayList<String> lista = (ArrayList<String>) documentSnapshot_proyecto.get("equipo");
                    equipo.clear();
                    id_equipo.clear();
                    adapter_equipo.notifyDataSetChanged();
                    if (!lista.isEmpty()){
                        for (String cadena : lista){
                            mUserProvider.getUser(cadena).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        if (documentSnapshot.contains("usuario")){
                                            String usuario = documentSnapshot.getString("usuario");
                                            equipo.add(usuario);
                                            adapter_equipo.notifyDataSetChanged();
                                        }
                                        if (documentSnapshot.contains("id")){
                                            String id = documentSnapshot.getString("id");
                                            id_equipo.add(id);
                                        }
                                    }
                                }
                            });
                        }
                    }
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
        tarea.setIdProyecto(id_proyecto);
        tarea.setIdUsuario(id_usuario);
        tarea.setCompletado(0);
        mDialog.show();
        mTareaProvider.save(tarea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    Intent intent = new Intent(RegistroTarea.this, TareaDetailActivity.class);
                    intent.putExtra("id", tarea.getId());
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