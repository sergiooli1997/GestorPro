package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Archivo;
import com.escom.gestorpro.providers.ArchivosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class NuevoArchivoActivity extends AppCompatActivity {
    String mExtraProyectoId;

    TextInputEditText mtextInputNombre;
    TextInputEditText mtextInputDescripcion;
    TextInputEditText mtextInputLink;
    Button btnCrear;
    ImageView mImageViewBack;
    AlertDialog mDialog;

    ArchivosProvider mArchivosProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_archivo);
        mArchivosProvider = new ArchivosProvider();

        mImageViewBack = findViewById(R.id.imageViewBack);
        mtextInputNombre = findViewById(R.id.textInputNombreArchivo);
        mtextInputDescripcion = findViewById(R.id.textInputDescripcion);
        mtextInputLink = findViewById(R.id.textInputLink);
        btnCrear = findViewById(R.id.btnAceptar);

        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearArchivo();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void crearArchivo() {
        Archivo archivo = new Archivo();
        archivo.setIdProyecto(mExtraProyectoId);
        archivo.setNombre(mtextInputNombre.getText().toString());
        archivo.setDescripcion(mtextInputDescripcion.getText().toString());
        archivo.setLink(mtextInputLink.getText().toString());

        mArchivosProvider.save(archivo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NuevoArchivoActivity.this, "Archivo creado correctamente", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}