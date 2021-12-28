package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.adapters.ProyectosMiEmpresaAdapter;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import dmax.dialog.SpotsDialog;

public class MiEmpresaActivity extends AppCompatActivity {
    ProyectoProvider mProyectosProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    ProyectosMiEmpresaAdapter mProyectosAdapter;

    RecyclerView mRecyclerProyectos;
    AlertDialog mDialog;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_empresa);

        mProyectosProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(MiEmpresaActivity.this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mDialog.show();
        mRecyclerProyectos =  findViewById(R.id.RecyclerViewProyectos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MiEmpresaActivity.this);
        mRecyclerProyectos.setLayoutManager(linearLayoutManager);

        mImageViewBack = findViewById(R.id.imageViewBack);

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final Query[] query = new Query[1];
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("rol")){
                        String rol = documentSnapshot.getString("rol");
                        if (rol.equals("Cliente")){
                            query[0] = mProyectosProvider.getProyectoByCliente(mAuthProvider.getUid());
                        }
                        else{
                            query[0] = mProyectosProvider.getProyectoByUser(mAuthProvider.getUid());
                        }
                        FirestoreRecyclerOptions<Proyecto> options = new FirestoreRecyclerOptions.Builder<Proyecto>()
                                .setQuery(query[0], Proyecto.class)
                                .build();
                        mProyectosAdapter = new ProyectosMiEmpresaAdapter(options, MiEmpresaActivity.this);
                        mRecyclerProyectos.setAdapter(mProyectosAdapter);
                        mProyectosAdapter.startListening();

                        mDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mProyectosAdapter.stopListening();
    }
}