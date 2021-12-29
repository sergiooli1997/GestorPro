package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.escom.gestorpro.R;
import com.escom.gestorpro.adapters.EstimacionAdapter;
import com.escom.gestorpro.models.Estimacion;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.EstimacionProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class EstimacionActivity extends AppCompatActivity {
    String mExtraProyectoId;
    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    EstimacionProvider mEstimacionProvider;
    ProyectoProvider mProyectoProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    EstimacionAdapter mEstimacionAdapter;

    FloatingActionButton fab;
    RecyclerView mRecyclerEstimacion;
    Spinner spinnerProyecto;
    AlertDialog mDialog;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimacion);

        mEstimacionProvider = new EstimacionProvider();
        mProyectoProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mRecyclerEstimacion = findViewById(R.id.RecyclerViewEstimaciones);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EstimacionActivity.this);
        mRecyclerEstimacion.setLayoutManager(linearLayoutManager);

        fab = findViewById(R.id.newEstimacion);

        spinnerProyecto = (Spinner)findViewById(R.id.spinnerProyectoEstimacion);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, proyectos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProyecto.setAdapter(adapter);

        mImageViewBack = findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDialog = new SpotsDialog.Builder()
                .setContext(EstimacionActivity.this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();
        mDialog.show();

        spinnerProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mExtraProyectoId = id_proyectos.get(spinnerProyecto.getSelectedItemPosition());
                Query[] query = new Query[1];
                query[0] = mEstimacionProvider.getEstimacionesByProyecto(mExtraProyectoId);

                FirestoreRecyclerOptions<Estimacion> options1 = new FirestoreRecyclerOptions.Builder<Estimacion>()
                        .setQuery(query[0], Estimacion.class)
                        .build();

                mEstimacionAdapter = new EstimacionAdapter(options1, EstimacionActivity.this);
                mRecyclerEstimacion.setAdapter(mEstimacionAdapter);
                mEstimacionAdapter.startListening();

                mDialog.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEstimacion();
            }
        });

        loadProyecto();
    }

    private void newEstimacion() {
        Intent miIntent = new Intent(EstimacionActivity.this, NuevaEstimacionActivity.class);
        miIntent.putExtra("idProyecto", mExtraProyectoId);
        startActivity(miIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEstimacionAdapter.stopListening();
    }

    private void loadProyecto() {
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists() && documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if (rol.equals("Cliente")){
                        mProyectoProvider.getProyectoByCliente(mAuthProvider.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    else{
                        mProyectoProvider.getProyectoByUser(mAuthProvider.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                }
            }
        });
    }
}