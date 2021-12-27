package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.adapters.RiesgoAdapter;
import com.escom.gestorpro.models.Riesgo;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.RiesgosProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import dmax.dialog.SpotsDialog;

public class RiesgoDetailActivity extends AppCompatActivity {
    String mExtraProyectoId;

    RiesgosProvider mRiesgosProvider;
    ProyectoProvider mProyectoProvider;

    RiesgoAdapter mRiesgoBajoAdapter;
    RiesgoAdapter mRiesgoMedioAdapter;
    RiesgoAdapter mRiesgoAltoAdapter;

    RecyclerView mRecyclerRiesgoBajo;
    RecyclerView mRecyclerRiesgoMedio;
    RecyclerView mRecyclerRiesgoAlto;
    FloatingActionButton fab;
    AlertDialog mDialog;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riesgo_detail);
        mRiesgosProvider = new RiesgosProvider();
        mProyectoProvider = new ProyectoProvider();
        mRecyclerRiesgoBajo = findViewById(R.id.RecyclerViewRiesgoBajo);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RiesgoDetailActivity.this);
        mRecyclerRiesgoBajo.setLayoutManager(linearLayoutManager);

        mRecyclerRiesgoMedio = findViewById(R.id.RecyclerViewRiesgoMedio);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(RiesgoDetailActivity.this);
        mRecyclerRiesgoMedio.setLayoutManager(linearLayoutManager2);

        mRecyclerRiesgoAlto = findViewById(R.id.RecyclerViewRiesgoAlto);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(RiesgoDetailActivity.this);
        mRecyclerRiesgoAlto.setLayoutManager(linearLayoutManager3);

        fab = findViewById(R.id.newRiesgo);
        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        mImageViewBack = findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDialog = new SpotsDialog.Builder()
                .setContext(RiesgoDetailActivity.this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();
        mDialog.show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRiesgo();
            }
        });
    }

    private void newRiesgo() {
        Intent miIntent = new Intent(RiesgoDetailActivity.this, NuevoRiesgoActivity.class);
        miIntent.putExtra("idProyecto", mExtraProyectoId);
        startActivity(miIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Query[] query = new Query[3];
        query[0] = mRiesgosProvider.getRiesgoByProyecto(mExtraProyectoId, "Bajo");
        query[1] = mRiesgosProvider.getRiesgoByProyecto(mExtraProyectoId, "Medio");
        query[2] = mRiesgosProvider.getRiesgoByProyecto(mExtraProyectoId, "Alto");

        FirestoreRecyclerOptions<Riesgo> options1 = new FirestoreRecyclerOptions.Builder<Riesgo>()
                .setQuery(query[0], Riesgo.class)
                .build();
        FirestoreRecyclerOptions<Riesgo>  options2 = new FirestoreRecyclerOptions.Builder<Riesgo>()
                .setQuery(query[1], Riesgo.class)
                .build();
        FirestoreRecyclerOptions<Riesgo>  options3 = new FirestoreRecyclerOptions.Builder<Riesgo>()
                .setQuery(query[2], Riesgo.class)
                .build();

        mRiesgoBajoAdapter = new RiesgoAdapter(options1, RiesgoDetailActivity.this);
        mRecyclerRiesgoBajo.setAdapter(mRiesgoBajoAdapter);
        mRiesgoBajoAdapter.startListening();

        mRiesgoMedioAdapter = new RiesgoAdapter(options2, RiesgoDetailActivity.this);
        mRecyclerRiesgoMedio.setAdapter(mRiesgoMedioAdapter);
        mRiesgoMedioAdapter.startListening();

        mRiesgoAltoAdapter = new RiesgoAdapter(options3, RiesgoDetailActivity.this);
        mRecyclerRiesgoAlto.setAdapter(mRiesgoAltoAdapter);
        mRiesgoAltoAdapter.startListening();

        mDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRiesgoBajoAdapter.stopListening();
        mRiesgoMedioAdapter.stopListening();
        mRiesgoAltoAdapter.stopListening();
    }
}