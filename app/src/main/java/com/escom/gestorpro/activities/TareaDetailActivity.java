package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.TareaProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class TareaDetailActivity extends AppCompatActivity {
    String mExtraTareaId;
    String mIdUser = "";
    String mIdProyecto = "";

    TextView textViewFechaInicio;
    TextView textViewFechaFin;
    TextView textViewTitle;
    TextView textViewDesc;
    TextView textViewUsuario;
    TextView textViewCel;
    TextView textViewNombreProy;
    TextView textViewRepositorio;
    CircleImageView circleImageViewProfile;
    Button btnVerPerfil;
    Button btnVerProyecto;
    Toolbar toolbar;

    TareaProvider mTareaProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_detail);

        mTareaProvider = new TareaProvider();

        textViewFechaInicio = findViewById(R.id.textViewRelativeTimeInicio);
        textViewFechaFin = findViewById(R.id.textViewRelativeTimeFinal);
        textViewTitle = findViewById(R.id.textViewTituloTarea);
        textViewDesc = findViewById(R.id.textViewDescr);
        textViewUsuario = findViewById(R.id.textViewUsuario);
        textViewCel = findViewById(R.id.textViewCel);
        textViewNombreProy = findViewById(R.id.textViewProyecto);
        textViewRepositorio = findViewById(R.id.textViewRepositorio);
        circleImageViewProfile = findViewById(R.id.circleImageTareaDetail);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
        btnVerProyecto = findViewById(R.id.btnVerProyecto);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mExtraTareaId = getIntent().getStringExtra("id");

        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        btnVerProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProyecto();
            }
        });

        getTarea();
    }

    private void getTarea() {
        mTareaProvider.getTareaById(mExtraTareaId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    if (documentSnapshot.contains("fecha_inicio")) {
                        long timestamp_inicio = documentSnapshot.getLong("fecha_inicio");
                        Date date1 = new Date(timestamp_inicio);
                        String fecha_inicio = formatter.format(date1);
                        textViewFechaInicio.setText("Fecha inicio: " + fecha_inicio);
                    }
                    if (documentSnapshot.contains("fecha_fin")) {
                        long timestamp_fin = documentSnapshot.getLong("fecha_fin");
                        Date date2 = new Date(timestamp_fin);
                        String fecha_fin = formatter.format(date2);
                        textViewFechaFin.setText("Fecha fin: " + fecha_fin);
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

    private void goToShowProyecto() {
        /*if (!mIdProyecto.equals("")){
            Intent intent = new Intent(TareaDetailActivity.this, ProyectoDetailActivity.class);
            intent.putExtra("id", mIdProyecto);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }*/
    }

    private void goToShowProfile() {
        /*if (!mIdUser.equals("")){
            Intent intent = new Intent(TareaDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("usuario", mIdUser);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }*/
    }
}