package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.escom.gestorpro.utils.RelativeTime;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProyectoDetailActivity extends AppCompatActivity {
    // TODO: Cliente califica proyecto
    String mExtraProyectoId;
    String mIdUser = "";

    ProyectoProvider mProyectoProvider;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;

    TextView textViewTitulo;
    TextView textViewCodigo;
    TextView textViewFechaInicio;
    TextView textViewFechaFin;
    TextView textViewUsuario;
    TextView textViewCel;
    CircleImageView circleImageViewProfile;
    Button btVerPerfil;
    Button btnEliminar;
    FloatingActionButton mFabCalificacion;
    RecyclerView mRecyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyecto_detail);

        mProyectoProvider = new ProyectoProvider();
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        textViewTitulo = findViewById(R.id.textViewTituloProyecto);
        textViewCodigo = findViewById(R.id.textViewCodigoProyecto);
        textViewFechaInicio = findViewById(R.id.textViewRelativeTimeInicio);
        textViewFechaFin = findViewById(R.id.textViewRelativeTimeFinal);
        textViewUsuario = findViewById(R.id.textViewUsuarioPD);
        textViewCel = findViewById(R.id.textViewCelPD);
        circleImageViewProfile = findViewById(R.id.circleImageProyectoDetail);
        btVerPerfil = findViewById(R.id.btnVerPerfil);
        btnEliminar = findViewById(R.id.btnEliminar);
        mFabCalificacion = findViewById(R.id.fabCalificacion);
        mRecyclerView = findViewById(R.id.RecyclerViewTareas);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mExtraProyectoId = getIntent().getStringExtra("id");

        btVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(mExtraProyectoId, mAuthProvider.getUid());
            }
        });

        getProyecto();

    }

    private void getProyecto() {
        mProyectoProvider.getProyectoById(mExtraProyectoId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                    if (documentSnapshot.contains("equipo")){
                        ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("equipo");
                        String liderId = list.get(0);
                        getUserInfo(liderId);
                    }
                    if (documentSnapshot.contains("nombre")){
                        String titulo = documentSnapshot.getString("nombre");
                        textViewTitulo.setText(titulo.toUpperCase());
                    }
                    if (documentSnapshot.contains("codigo")){
                        String codigo = documentSnapshot.getString("codigo");
                        textViewCodigo.setText("Código para unirse: " + codigo);
                    }
                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("id")) {
                        mIdUser = documentSnapshot.getString("id");
                    }
                    if (documentSnapshot.contains("usuario")) {
                        String usuario = documentSnapshot.getString("usuario");
                        textViewUsuario.setText(usuario);
                    }
                    if (documentSnapshot.contains("celular")) {
                        String phone = documentSnapshot.getString("celular");
                        textViewCel.setText(phone);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        Picasso.with(ProyectoDetailActivity.this).load(imageProfile).into(circleImageViewProfile);
                    }
                }
            }
        });
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if (rol.equals("Líder de proyecto")){
                        btnEliminar.setText("Eliminar proyecto");
                    }
                    else{
                        btnEliminar.setText("Abandonar proyecto");
                    }
                }
            }
        });
    }

    private void goToShowProfile() {
        if (!mIdUser.equals("")){
            Intent intent = new Intent(ProyectoDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("usuario", mIdUser);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }

    }

    private void showConfirmDelete(String idProyecto, String idUsuario) {
        new AlertDialog.Builder(ProyectoDetailActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar proyecto")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProyecto(idProyecto, idUsuario);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deleteProyecto(String idProyecto, String idUsuario) {
        mProyectoProvider.getProyectoById(mExtraProyectoId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.contains("equipo")){
                    ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("equipo");
                    String liderId = list.get(0);
                    if (liderId.equals(idUsuario)){
                        mProyectoProvider.deleteProyecto(idProyecto);
                        Toast.makeText(ProyectoDetailActivity.this, "Se eliminó el proyecto", Toast.LENGTH_LONG).show();
                    }
                    else{
                        mProyectoProvider.deleteUsuarioFromProyecto(idProyecto, idUsuario);
                        Toast.makeText(ProyectoDetailActivity.this, "Abandonaste el proyecto", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(ProyectoDetailActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}