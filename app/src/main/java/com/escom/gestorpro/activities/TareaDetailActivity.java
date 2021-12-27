package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.AuthProvider;
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

public class TareaDetailActivity extends AppCompatActivity {
    String mExtraTareaId;
    String mIdUser = "";
    String mIdProyecto = "";

    UserProvider mUserProvider;
    ProyectoProvider mProyectoProvider;
    TareaProvider mTareaProvider;
    AuthProvider mAuthProvider;

    TextView textViewFechaInicio;
    TextView textViewFechaFin;
    TextView textViewTitle;
    TextView textViewDesc;
    TextView textViewUsuario;
    TextView textViewCel;
    TextView textViewNombreProy;
    TextView textViewRepositorio;
    TextView textViewPrioridad;
    CircleImageView circleImageViewProfile;
    ImageView mImageViewBack;
    Button btnVerPerfil;
    Button btnVerProyecto;
    Button btnCompletado;
    Button btnEliminarTarea;
    Toolbar toolbar;
    LinearLayout mLinearLayoutEditTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_detail);

        mTareaProvider = new TareaProvider();
        mUserProvider = new UserProvider();
        mProyectoProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();

        textViewFechaInicio = findViewById(R.id.textViewRelativeTimeInicio);
        textViewFechaFin = findViewById(R.id.textViewRelativeTimeFinal);
        textViewTitle = findViewById(R.id.textViewTituloTarea);
        textViewDesc = findViewById(R.id.textViewDescr);
        textViewUsuario = findViewById(R.id.textViewUsuario);
        textViewCel = findViewById(R.id.textViewCel);
        textViewNombreProy = findViewById(R.id.textViewProyecto);
        textViewRepositorio = findViewById(R.id.textViewRepositorio);
        textViewPrioridad = findViewById(R.id.textViewPrioridad);
        circleImageViewProfile = findViewById(R.id.circleImageTareaDetail);
        mImageViewBack = findViewById(R.id.imageViewBack);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
        btnVerProyecto = findViewById(R.id.btnVerProyecto);
        btnCompletado = findViewById(R.id.btnCompletado);
        btnEliminarTarea = findViewById(R.id.btnEliminarTarea);
        toolbar = findViewById(R.id.toolbar);
        mLinearLayoutEditTarea = findViewById(R.id.linearLayoutEditTarea);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        btnEliminarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(mExtraTareaId);
            }
        });

        mLinearLayoutEditTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditTarea();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkTareaCompletada();
        getTarea();
        checkUser();
    }

    private void checkUser() {
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if(rol.equals("Líder de proyecto")){
                        mLinearLayoutEditTarea.setVisibility(View.VISIBLE);
                        btnEliminarTarea.setVisibility(View.VISIBLE);
                    }
                    else{
                        mLinearLayoutEditTarea.setVisibility(View.GONE);
                        btnEliminarTarea.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showConfirmDelete(String idTarea) {
        new AlertDialog.Builder(TareaDetailActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar tarea")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarTarea(idTarea);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void eliminarTarea(String idTarea) {
        mTareaProvider.delete(idTarea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(TareaDetailActivity.this, "Se eliminó la tarea", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(TareaDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToEditTarea() {
        Intent intent = new Intent(TareaDetailActivity.this, EditTareaActivity.class);
        intent.putExtra("idTarea", mExtraTareaId);
        startActivity(intent);
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
                    if (documentSnapshot.contains("prioridad")){
                        String prioridad = documentSnapshot.getString("prioridad");
                        textViewPrioridad.setText("Prioridad " + prioridad);
                        if (prioridad.equals("Alta")){
                            textViewPrioridad.setTextColor(Color.parseColor("#E10E0E"));
                        }
                        else if (prioridad.equals("Media")){
                            textViewPrioridad.setTextColor(Color.parseColor("#FFC107"));
                        }
                        else{
                            textViewPrioridad.setTextColor(Color.parseColor("#4CAF50"));
                        }
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