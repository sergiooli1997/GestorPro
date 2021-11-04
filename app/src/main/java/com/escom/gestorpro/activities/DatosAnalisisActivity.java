package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import weka.*;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Analisis;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatosAnalisisActivity extends AppCompatActivity {

    String id_proyecto = "";
    int total_tareas = 0;
    double tareas_retraso_analisis = 0;
    double tareas_repo_vacio_analisis = 0;

    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ProyectoProvider mProyectoProvider;
    AuthProvider mAuthProvider;
    TareaProvider mTareaProvider;
    PostProvider mPostProvider;
    UserProvider mUserProvider;

    Spinner spinnerProyecto;
    TextView mTextRetraso;
    TextView mTextRepo;
    TextView mTextPosts;
    TextView mTextPostsOracion;
    TextView mTextChats;
    Button mAnalizar;
    ImageView mImageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_analisis);

        mProyectoProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mTareaProvider = new TareaProvider();
        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();

        mTextRetraso = findViewById(R.id.tareasRetrasoAnalisis);
        mTextRepo = findViewById(R.id.tareasRepositorioAnalisis);
        mTextPosts = findViewById(R.id.postsAnalisis);
        mTextPostsOracion = findViewById(R.id.postText);
        mTextChats = findViewById(R.id.chatsAnalisis);
        mAnalizar = findViewById(R.id.btnAnalizar);
        mImageViewBack = findViewById(R.id.imageViewBack);

        spinnerProyecto = (Spinner)findViewById(R.id.spinnerProyectoAnalisis);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, proyectos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProyecto.setAdapter(adapter);

        loadProyecto();

        spinnerProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_proyecto = id_proyectos.get(spinnerProyecto.getSelectedItemPosition());
                tareasRetraso(id_proyecto);
                tareasRepo(id_proyecto);
                getPost(id_proyecto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearAnalisis();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void crearAnalisis() {
        double retraso = Double.parseDouble(mTextRetraso.getText().toString());
        double repo_vacio = Double.parseDouble(mTextRepo.getText().toString());
        int post = Integer.parseInt(mTextPosts.getText().toString());


    }

    private void tareasRetraso(String id_proyecto) {
        mTareaProvider.getTareasTotalByProyecto(id_proyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    total_tareas = queryDocumentSnapshots.size();
                    if(total_tareas > 0){
                        mTareaProvider.getTareasConRetraso(id_proyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null){
                                    int tareas_retraso = value.size();
                                    tareas_retraso_analisis = (tareas_retraso*100.00)/total_tareas;
                                    mTextRetraso.setText(String.format("%.2f", tareas_retraso_analisis));
                                }
                            }
                        });
                    }
                    else{
                        mTextRetraso.setText("0");
                    }
                }
            }
        });
    }

    private void tareasRepo(String id_proyecto) {
        mTareaProvider.getTareasTotalByProyecto(id_proyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    total_tareas = queryDocumentSnapshots.size();
                    if(total_tareas > 0){
                        mTareaProvider.getTareasRepoVacio(id_proyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error == null){
                                    int tareas_vacias = value.size();
                                    tareas_repo_vacio_analisis = (tareas_vacias*100.00)/total_tareas;
                                    mTextRepo.setText(String.format("%.2f", tareas_repo_vacio_analisis));
                                }
                            }
                        });
                    }
                    else{
                        mTextRepo.setText("0");
                    }
                }
            }
        });
    }

    private void getPost(String idProyecto){
        mPostProvider.getPostByProyecto(idProyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    int total_post = queryDocumentSnapshots.size();
                    if(total_post == 1){
                        mTextPostsOracion.setText("publicación");
                    }
                    else{
                        mTextPostsOracion.setText("publicaciones");
                    }
                    mTextPosts.setText(String.valueOf(total_post));
                }
            }
        });
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