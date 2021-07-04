package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ImageProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class NuevoPost extends AppCompatActivity {
    CircleImageView mCircleImageViewBack;
    ImageView mImageViewPost;
    File mImageFile;
    Button mButtonPost;
    ImageProvider mImagePrivder;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputDesc;
    TextView mTextViewUsuario;

    String descripcion = "";

    private final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        mImagePrivder = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mImageViewPost = findViewById(R.id.subirImagen);
        mTextInputDesc = findViewById(R.id.textInputPost);
        mTextViewUsuario = findViewById(R.id.usuarioname);
        mButtonPost = findViewById(R.id.btnPublicar);

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });

        mImageViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickPost() {
        descripcion = mTextInputDesc.getText().toString();
        if (mImageFile != null){
            saveImage();
        }
    }

    private void saveImage() {
        mImagePrivder.save(NuevoPost.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    if (taskSave.isSuccessful()){
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
                    Toast.makeText(NuevoPost.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try{
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }
            catch (Exception e){
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}