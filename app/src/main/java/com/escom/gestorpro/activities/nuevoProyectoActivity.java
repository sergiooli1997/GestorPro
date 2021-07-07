package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.escom.gestorpro.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class nuevoProyectoActivity extends AppCompatActivity {
    CircleImageView mCircleImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_proyecto);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}