package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

//TODO: boton back no debe regresar a Menu.Activity
public class TareaDetailActivity extends AppCompatActivity {
    String mExtraTareaId;
    String mIdUser = "";
    String mIdProyecto = "";

    UserProvider mUserProvider;
    ProyectoProvider mProyectoProvider;
    TareaProvider mTareaProvider;

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
    Button btnCompletado;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_detail);

        mTareaProvider = new TareaProvider();
        mUserProvider = new UserProvider();
        mProyectoProvider = new ProyectoProvider();

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
        btnCompletado = findViewById(R.id.btnCompletado);
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

        btnCompletado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completado(mExtraTareaId);
            }
        });

        checkTareaCompletada();
        getTarea();
    }

    private void checkTareaCompletada() {
        mTareaProvider.getTareaById(mExtraTareaId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("completado")) {
                        int avance = documentSnapshot.getLong("completado").intValue();
                        if (avance == 0) {
                            btnCompletado.setText("Marcar tarea como completada");
                        }
                        else{
                            btnCompletado.setText("Marcar tarea como incompleta");
                        }
                    }
                }
            }
        });
    }

    private void completado(String id) {
        final int[] value = new int[1];
        mTareaProvider.getTareaById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("completado")){
                        int avance = documentSnapshot.getLong("completado").intValue();
                        if (avance == 0){
                            //Tarea marcada como incompleta
                            value[0] = 1;
                        }
                        else{
                            //Tarea marcada como completa
                            value[0] = 0;
                        }
                        mTareaProvider.updateAvance(id, value[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if (value[0] == 1){
                                        btnCompletado.setText("Marcar tarea como incompleta");
                                    }
                                    else{
                                        btnCompletado.setText("Marcar tarea como completada");
                                    }
                                    Toast.makeText(TareaDetailActivity.this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getProyectoInfo(String mIdProyecto) {
        mProyectoProvider.getProyectoById(mIdProyecto).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombre")){
                        String nombreProy = documentSnapshot.getString("nombre");
                        textViewNombreProy.setText(nombreProy);
                    }
                }
            }
        });
    }

    private void getUserInfo(String mIdUser) {
        mUserProvider.getUser(mIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("usuario")){
                        String usuario = documentSnapshot.getString("usuario");
                        textViewUsuario.setText(usuario);
                    }
                    if (documentSnapshot.contains("celular")){
                        String cel = documentSnapshot.getString("celular");
                        textViewCel.setText(cel);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(TareaDetailActivity.this).load(imageProfile).into(circleImageViewProfile);
                            }
                        }
                    }
                }
            }
        });
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
                    if (documentSnapshot.contains("idUsuario")){
                        mIdUser = documentSnapshot.getString("idUsuario");
                        getUserInfo(mIdUser);
                    }
                    if (documentSnapshot.contains("idProyecto")){
                        mIdProyecto = documentSnapshot.getString("idProyecto");
                        getProyectoInfo(mIdProyecto);
                    }
                }
            }
        });
    }

    private void goToShowProyecto() {
        if (!mIdProyecto.equals("")){
            Intent intent = new Intent(TareaDetailActivity.this, ProyectoDetailActivity.class);
            intent.putExtra("id", mIdProyecto);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }
    }

    private void goToShowProfile() {
        if (!mIdUser.equals("")){
            Intent intent = new Intent(TareaDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("usuario", mIdUser);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }
    }
}