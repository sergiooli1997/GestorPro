package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ImageProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.escom.gestorpro.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class NuevoPost extends AppCompatActivity {
    private final int GALLERY_REQUEST_CODE = 1;
    private final int PHOTO_REQUEST_CODE = 3;
    String id_proyecto = "";
    String descripcion = "";
    ArrayAdapter<String> adapter;
    CharSequence options[];
    List<String> proyectos = new ArrayList<>();
    List<String> id_proyectos = new ArrayList<>();

    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;
    File mImageFile;

    ImageView mImageViewBack;
    ImageView mImageViewPost;
    Button mButtonPost;
    Spinner spinnerProyecto;
    TextInputEditText mTextInputDesc;
    TextView mTextViewUsuario;
    CircleImageView mCircleImgUsuario;

    AlertDialog mDialog;
    AlertDialog.Builder mBuilderSelector;

    ImageProvider mImagePrivder;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    ProyectoProvider mProyectoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        mImagePrivder = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mProyectoProvider = new ProyectoProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opción");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};

        spinnerProyecto = (Spinner)findViewById(R.id.spinnerProyecto);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, proyectos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProyecto.setAdapter(adapter);

        mImageViewBack = findViewById(R.id.imageViewBack);
        mImageViewPost = findViewById(R.id.subirImagen);
        mTextInputDesc = findViewById(R.id.textInputPost);
        mTextViewUsuario = findViewById(R.id.usuarioname);
        mCircleImgUsuario = findViewById(R.id.circleImageUser);
        mButtonPost = findViewById(R.id.btnPublicar);

        getUserInfo(mAuthProvider.getUid());
        loadProyecto();

        spinnerProyecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_proyecto = id_proyectos.get(spinnerProyecto.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });

        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(GALLERY_REQUEST_CODE);
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadProyecto() {
        mProyectoProvider.getProyectoByUser2(mAuthProvider.getUid()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private void getUserInfo(String uid) {
        mUserProvider.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("usuario")){
                        String usuario = documentSnapshot.getString("usuario");
                        mTextViewUsuario.setText(usuario);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(NuevoPost.this).load(imageProfile).into(mCircleImgUsuario);
                            }
                        }
                    }
                }
            }
        });
    }

    private void selectOptionImage(int requestCode) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0 ){
                    openGallery(requestCode);
                }
                else if (i == 1){
                    takePhoto();
                }
            }
        });

        mBuilderSelector.show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            } catch(Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(NuevoPost.this, "com.escom.gestorpro", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
            }
        }
    }

    private File createPhotoFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        mPhotoPath = "file:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private void clickPost() {
        descripcion = mTextInputDesc.getText().toString();
        //SELECCIONO FOTO DE GALERIA
        if (mImageFile != null){
            saveImage(mImageFile);
        }
        //TOMO FOTO DESDE CAMARA
        else if (mPhotoFile != null){
            saveImage(mPhotoFile);
        }
        else{
            Toast.makeText(NuevoPost.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(File imageFile) {
        mDialog.show();
        mImagePrivder.save(NuevoPost.this, imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImagePrivder.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage(url);
                            post.setTexto(descripcion);
                            post.setUsuario(mAuthProvider.getUid());
                            post.setFecha(new Date().getTime());
                            post.setIdProyecto(id_proyecto);
                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    if (taskSave.isSuccessful()){
                                        mDialog.dismiss();
                                        Intent intent = new Intent(NuevoPost.this, MenuActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(NuevoPost.this, "La información se almacenó correctamente", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(NuevoPost.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(NuevoPost.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try{
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }
            catch (Exception e){
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mPhotoFile = new File(mAbsolutePhotoPath);
            mImageFile = null;
            Picasso.with(NuevoPost.this).load(mPhotoPath).into(mImageViewPost);
        }
    }
}