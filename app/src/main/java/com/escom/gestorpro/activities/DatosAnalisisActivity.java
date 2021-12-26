package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import java.io.*;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    double post_avisos_analisis = 0;

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
    TextView mTextCompletas;
    TextView mTextRepo;
    TextView mTextPosts;
    TextView mTextPosts2;
    TextView mTextClasificacion;
    Button mAnalizar;
    ImageView mImageViewBack;
    PieChart pieChart1;
    PieChart pieChart2;
    PieChart pieChart3;
    PieChart pieChart4;

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
        mTextPosts2 = findViewById(R.id.postsAvisoAnalisis);
        mTextCompletas = findViewById(R.id.tareasIncompletasAnalisis);
        mTextClasificacion = findViewById(R.id.clasificacionAnalisis);
        mAnalizar = findViewById(R.id.btnAnalizar);
        mImageViewBack = findViewById(R.id.imageViewBack);
        pieChart1 = findViewById(R.id.pieChart1);
        pieChart2 = findViewById(R.id.pieChart2);
        pieChart3 = findViewById(R.id.pieChart3);
        pieChart4 = findViewById(R.id.pieChart4);

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
                tareasCompletas(id_proyecto);
                tareasRepo(id_proyecto);
                getPost(id_proyecto);
                getAvisoPost(id_proyecto);
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
        double completo = Double.parseDouble(mTextCompletas.getText().toString());
        double post = Double.parseDouble(mTextPosts.getText().toString());
        double postAviso = Double.parseDouble(mTextPosts2.getText().toString());
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

        crearGraficoRetraso(retraso);
        crearGraficoCompletas(completo);
        crearGraficoRepo(repo_vacio);
        crearGraficoPost(post, postAviso);

    }

    private void crearGraficoPost(double post, double postAviso) {
        Description description = new Description();
        description.setText("% Publicaciones");
        description.setTextSize(16f);
        description.setTextColor(Color.WHITE);
        pieChart4.setDescription(description);
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) post, "Post criticos"));
        pieEntries.add(new PieEntry((float) postAviso, "Post avisos"));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        PieData pieData = new PieData(pieDataSet);
        pieChart4.setData(pieData);
        pieChart4.setEntryLabelColor(Color.WHITE);
        pieChart4.setEntryLabelTextSize(16f);
        pieChart4.setHoleColor(Color.BLACK);
        pieChart4.getLegend().setEnabled(false);
        pieChart4.invalidate();
    }

    private void crearGraficoRepo(double repo_vacio) {
        Description description = new Description();
        description.setText("% Tareas");
        description.setTextSize(16f);
        description.setTextColor(Color.WHITE);
        pieChart3.setDescription(description);
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) repo_vacio, "Sin repositorio"));
        pieEntries.add(new PieEntry((float) (100-repo_vacio), "Con repositorio"));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        PieData pieData = new PieData(pieDataSet);
        pieChart3.setData(pieData);
        pieChart3.setEntryLabelColor(Color.WHITE);
        pieChart3.setEntryLabelTextSize(16f);
        pieChart3.setHoleColor(Color.BLACK);
        pieChart3.getLegend().setEnabled(false);
        pieChart3.invalidate();
    }

    private void crearGraficoCompletas(double completo) {
        Description description = new Description();
        description.setText("% Tareas");
        description.setTextSize(16f);
        description.setTextColor(Color.WHITE);
        pieChart2.setDescription(description);
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) completo, "Completas"));
        pieEntries.add(new PieEntry((float) (100-completo), "No completadas"));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        PieData pieData = new PieData(pieDataSet);
        pieChart2.setData(pieData);
        pieChart2.setEntryLabelColor(Color.WHITE);
        pieChart2.setEntryLabelTextSize(16f);
        pieChart2.setHoleColor(Color.BLACK);
        pieChart2.getLegend().setEnabled(false);
        pieChart2.invalidate();
    }

    private void crearGraficoRetraso(double retraso) {
        Description description = new Description();
        description.setText("% Tareas de alta prioridad");
        description.setTextSize(16f);
        description.setTextColor(Color.WHITE);
        pieChart1.setDescription(description);
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float) retraso, "Con retraso"));
        pieEntries.add(new PieEntry((float) (100-retraso), "Sin retraso"));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);
        PieData pieData = new PieData(pieDataSet);
        pieChart1.setData(pieData);
        pieChart1.setEntryLabelColor(Color.WHITE);
        pieChart1.setEntryLabelTextSize(16f);
        pieChart1.setHoleColor(Color.BLACK);
        pieChart1.getLegend().setEnabled(false);
        pieChart1.invalidate();
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
                                    mTextRetraso.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    else{
                        mTextRetraso.setText("0");
                        mTextRetraso.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void tareasCompletas(String id_proyecto) {
        mTareaProvider.getTareaCompletoByProyecto(id_proyecto, 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    int tareas_completadas = queryDocumentSnapshots.size();
                    if (total_tareas != 0){
                        double completas = (tareas_completadas*100.00)/total_tareas;
                        mTextCompletas.setText(String.valueOf(completas));
                        mTextCompletas.setVisibility(View.GONE);
                    }
                    else{
                        mTextCompletas.setText("0");
                        mTextCompletas.setVisibility(View.GONE);
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
                                    mTextRepo.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    else{
                        mTextRepo.setText("0");
                        mTextRepo.setVisibility(View.GONE);
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
                                    mTextPosts.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    else{
                        mTextPosts.setText("0");
                        mTextPosts.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void getAvisoPost(String id_proyecto){
        mPostProvider.getPostByProyecto(id_proyecto).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    total_post = queryDocumentSnapshots.size();
                    if (total_post > 0){
                        mPostProvider.getPostCriticosByProyecto(id_proyecto, "Aviso").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error == null){
                                    int post_aviso = value.size();
                                    post_avisos_analisis = (post_aviso * 100)/ total_post;
                                    mTextPosts2.setText(String.valueOf(post_avisos_analisis));

                                }
                            }
                        });
                    }
                    else{
                        mTextPosts2.setText(String.valueOf(0));
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