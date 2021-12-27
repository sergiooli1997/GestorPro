package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Riesgo;
import com.escom.gestorpro.providers.RiesgosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class NuevoRiesgoActivity extends AppCompatActivity {
    String probabilidad = "";
    String impacto = "";
    String mExtraProyectoId;

    TextInputEditText mtextInputNombre;
    TextInputEditText mtextInputLink;
    Spinner spinnerProbabilidad;
    Spinner spinnerImpacto;
    Button btnCrear;
    ImageView mImageViewBack;
    AlertDialog mDialog;

    RiesgosProvider mRiesgosProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_riesgo);

        mRiesgosProvider = new RiesgosProvider();

        mImageViewBack = findViewById(R.id.imageViewBack);
        mtextInputNombre = findViewById(R.id.textInputNombreRiesgo);
        mtextInputLink = findViewById(R.id.textInputLink);
        spinnerProbabilidad = (Spinner)findViewById(R.id.spinerProbabilidad);
        spinnerImpacto = (Spinner)findViewById(R.id.spinerImpacto);
        btnCrear = findViewById(R.id.btnAceptar);

        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        ArrayAdapter<CharSequence> adapter_probabilidad = ArrayAdapter.createFromResource(this,
                R.array.probabilidad, android.R.layout.simple_spinner_item);
        adapter_probabilidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProbabilidad.setAdapter(adapter_probabilidad);

        ArrayAdapter<CharSequence> adapter_impacto = ArrayAdapter.createFromResource(this,
                R.array.impacto, android.R.layout.simple_spinner_item);
        adapter_probabilidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImpacto.setAdapter(adapter_impacto);

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearRiesgo();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerProbabilidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerProbabilidad.getSelectedItem().toString().equals("Seleccione probabilidad")){
                    probabilidad = "Raro";
                }
                else{
                    probabilidad = spinnerProbabilidad.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerImpacto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerImpacto.getSelectedItem().toString().equals("Seleccione impacto")){
                    impacto = "Despreciable";
                }
                else{
                    impacto = spinnerImpacto.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void crearRiesgo() {
        Riesgo riesgo = new Riesgo();
        riesgo.setNombre(mtextInputNombre.getText().toString());
        riesgo.setProbabilidad(probabilidad);
        riesgo.setImpacto(impacto);
        riesgo.setIdProyecto(mExtraProyectoId);
        riesgo.setLink(mtextInputLink.getText().toString());
        String clasif = calcularClasificacion(probabilidad, impacto);
        riesgo.setClasificacion(clasif);
        mRiesgosProvider.save(riesgo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NuevoRiesgoActivity.this, "Riesgo registrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(NuevoRiesgoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String calcularClasificacion(String probabilidad, String impacto) {
        switch (probabilidad) {
            case "Raro":
                if (impacto.equals("Despreciable") || impacto.equals("Menor")) {
                    return "Bajo";
                } else {
                    return "Medio";
                }
            case "Poco probable":
                if (impacto.equals("Despreciable") || impacto.equals("Menor")) {
                    return "Bajo";
                } else if (impacto.equals("Moderado") || impacto.equals("Mayor")) {
                    return "Medio";
                } else {
                    return "Alto";
                }
            case "Posible":
                if (impacto.equals("Despreciable")) {
                    return "Bajo";
                } else if (impacto.equals("Menor") || impacto.equals("Moderado")) {
                    return "Medio";
                } else {
                    return "Alto";
                }
            default:
                if (impacto.equals("Despreciable") || impacto.equals("Menor")) {
                    return "Medio";
                } else {
                    return "Alto";
                }
        }
    }
}