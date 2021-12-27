package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Buzon;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.BuzonProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuzonActivity extends AppCompatActivity {
    String id_proyecto = "";
    String url = "";
    Spinner spinnerProyecto;
    Button mButtonAsignar;
    Button mButtonModificar;
    Button mButtonConsultar;

    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ImageView mImageViewBack;

    ProyectoProvider mProyectoProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    BuzonProvider mBuzonProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon);

        mProyectoProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mBuzonProvider = new BuzonProvider();

        mImageViewBack = findViewById(R.id.imageViewBack);
        mButtonAsignar = findViewById(R.id.btnAsignar);
        mButtonModificar = findViewById(R.id.btnModificar);
        mButtonConsultar = findViewById(R.id.btnConsultar);

        spinnerProyecto = (Spinner)findViewById(R.id.spinnerProyecto);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, proyectos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProyecto.setAdapter(adapter);

        loadProyecto();
        checkUser();

        spinnerProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_proyecto = id_proyectos.get(spinnerProyecto.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButtonAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBuzon();
            }
        });

        mButtonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_proyecto!=null){
                    showConfirmDelete(id_proyecto);
                }
            }
        });

        mButtonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuzonProvider.getBuzonByProyecto(id_proyecto).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                url = document.getString("link");
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }

    private void showConfirmDelete(String id_proyecto) {
        new AlertDialog.Builder(BuzonActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar post")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarBuzon(id_proyecto);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void eliminarBuzon(String id_proyecto) {
        mBuzonProvider.getBuzonByProyecto(id_proyecto).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getString("id");
                        mBuzonProvider.deleteBuzon(id).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(BuzonActivity.this, "Se elimino link", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(BuzonActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void checkUser() {
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists() && documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if (rol.equals("Líder de proyecto")){
                        mButtonAsignar.setVisibility(View.VISIBLE);
                        mButtonModificar.setVisibility(View.VISIBLE);
                    }
                    else{
                        mButtonAsignar.setVisibility(View.GONE);
                        mButtonModificar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showDialogBuzon() {
        AlertDialog.Builder alert = new AlertDialog.Builder(BuzonActivity.this);
        alert.setTitle("Buzón de quejas y felicitaciones");
        alert.setMessage("Ingresa tu comentario");

        final EditText editText = new EditText(BuzonActivity.this);
        editText.setHint("Escribe el link del buzón");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(BuzonActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString();
                if (!value.isEmpty()){
                    createBuzon(value);
                }
                else{
                    Toast.makeText(BuzonActivity.this, "Debe ingesar link", Toast.LENGTH_LONG).show();
                }

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void createBuzon(String value) {
        Buzon buzon = new Buzon();
        buzon.setLink(value);
        buzon.setIdProyecto(id_proyecto);
        mBuzonProvider.save(buzon).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(BuzonActivity.this, "Link asignado correctamente", Toast.LENGTH_LONG).show();
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