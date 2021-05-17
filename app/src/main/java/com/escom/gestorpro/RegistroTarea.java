package com.escom.gestorpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroTarea extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    TextInputEditText mtextInputNombreEvento;
    TextInputEditText mtextInputFechaInicio;
    TextInputEditText mtextInputFechaFinal;
    Button mButtonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tarea);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mtextInputNombreEvento = findViewById(R.id.textInputNombreEvento);
        mtextInputFechaInicio = findViewById(R.id.textInputFechaInicio);
        mtextInputFechaFinal = findViewById(R.id.textInputFechaFinal);
        mButtonRegister = findViewById(R.id.btnAceptar);

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}