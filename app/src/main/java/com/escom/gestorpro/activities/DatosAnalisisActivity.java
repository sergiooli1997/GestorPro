package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import java.io.*;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.escom.gestorpro.providers.FileProvider;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DatosAnalisisActivity extends AppCompatActivity {

    String id_proyecto = "";
    int total_tareas = 0;
    int total_post = 0;
    double tareas_retraso_analisis = 0;
    double tareas_repo_vacio_analisis = 0;
    double post_criticos_analisis = 0;

    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ProyectoProvider mProyectoProvider;
    AuthProvider mAuthProvider;
    TareaProvider mTareaProvider;
    PostProvider mPostProvider;
    UserProvider mUserProvider;
    FileProvider mFileProvider;

    Spinner spinnerProyecto;
    TextView mTextRetraso;
    TextView mTextRepo;
    TextView mTextPosts;
    TextView mTextPostsOracion;
    TextView mTextClasificacion;
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
        mFileProvider = new FileProvider();

        mTextRetraso = findViewById(R.id.tareasRetrasoAnalisis);
        mTextRepo = findViewById(R.id.tareasRepositorioAnalisis);
        mTextPosts = findViewById(R.id.postsAnalisis);
        mTextPostsOracion = findViewById(R.id.postText);
        mTextClasificacion = findViewById(R.id.clasificacionAnalisis);
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
                loadFile();
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

    private void loadFile(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mFileProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            InputStream input = new URL(uri.toString()).openStream();
                            CSVLoader loader = new CSVLoader();
                            loader.setSource(input);
                            Instances trainingDataSet = loader.getDataSet();
                            /*trainingDataSet.setClassIndex(trainingDataSet.numAttributes()-1);

                            J48 tree = new J48();
                            tree.buildClassifier(trainingDataSet);

                            double[] values = new double[trainingDataSet.numAttributes()];
                            values[0] = Double.parseDouble(mTextRetraso.getText().toString());
                            values[1] = Double.parseDouble(mTextRepo.getText().toString());
                            values[2] = Integer.parseInt(mTextPosts.getText().toString());
                            values[3] = trainingDataSet.attribute(3).addStringValue("?");
                            DenseInstance testInstance = new DenseInstance(1.0, values);
                            testInstance.setDataset(trainingDataSet);

                            double predB = tree.classifyInstance(testInstance);
                            String predString = testInstance.classAttribute().value((int) predB);
                            Toast.makeText(DatosAnalisisActivity.this, predString, Toast.LENGTH_SHORT).show();*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private void crearAnalisis() {
        double retraso = Double.parseDouble(mTextRetraso.getText().toString());
        double repo_vacio = Double.parseDouble(mTextRepo.getText().toString());
        double post = Double.parseDouble(mTextPosts.getText().toString());
        String clasificacion;
        if (retraso <= 23.53){
            clasificacion = "Productivo";
        }
        else{
            if (retraso <= 30){
                if(repo_vacio <= 5.5){
                    clasificacion = "Productivo";
                }
                else{
                    clasificacion = "No productivo";
                }
            }
            else{
                clasificacion = "No productivo";
            }
        }
        mTextClasificacion.setText("Clasficación: " + clasificacion);
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
                if (error == null){
                    total_post = queryDocumentSnapshots.size();
                    if (total_post > 0){
                        mPostProvider.getPostCriticosByProyecto(idProyecto, "Critico").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null){
                                    int post_criticos = value.size();
                                    post_criticos_analisis = (post_criticos * 100)/ total_post;
                                    mTextPosts.setText(String.format("%.2f", post_criticos_analisis));
                                }
                            }
                        });
                    }
                    else{
                        mTextPosts.setText("0");
                    }
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