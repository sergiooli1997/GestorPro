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
import com.escom.gestorpro.adapters.ArchivosAdapter;
import com.escom.gestorpro.models.Archivo;
import com.escom.gestorpro.providers.ArchivosProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MiEmpresaDetailActivity extends AppCompatActivity {
    String mExtraProyectoId;

    ProyectoProvider mProyectoProvider;
    ArchivosProvider mArchivosProvider;

    ArchivosAdapter mArchivosAdapter;

    RecyclerView mRecyclerViewArchivos;
    FloatingActionButton fab;
    AlertDialog mDialog;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_empresa_detail);

        mProyectoProvider = new ProyectoProvider();
        mArchivosProvider = new ArchivosProvider();
        mRecyclerViewArchivos = findViewById(R.id.RecyclerViewArchivos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MiEmpresaDetailActivity.this);
        mRecyclerViewArchivos.setLayoutManager(linearLayoutManager);

        fab = findViewById(R.id.newArchivo);
        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        mImageViewBack = findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDialog = new SpotsDialog.Builder()
                .setContext(MiEmpresaDetailActivity.this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();
        mDialog.show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newArchivo();
            }
        });
    }

    private void newArchivo() {
        Intent miIntent = new Intent(MiEmpresaDetailActivity.this, NuevoArchivoActivity.class);
        miIntent.putExtra("idProyecto", mExtraProyectoId);
        startActivity(miIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Query[] query = new Query[1];
        query[0] = mArchivosProvider.getArchivosByProyecto(mExtraProyectoId);

        FirestoreRecyclerOptions<Archivo> options1 = new FirestoreRecyclerOptions.Builder<Archivo>()
                .setQuery(query[0], Archivo.class)
                .build();

        mArchivosAdapter = new ArchivosAdapter(options1, MiEmpresaDetailActivity.this);
        mRecyclerViewArchivos.setAdapter(mArchivosAdapter);
        mArchivosAdapter.startListening();

        mDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mArchivosAdapter.stopListening();
    }
}