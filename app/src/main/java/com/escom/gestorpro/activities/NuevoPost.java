package com.escom.gestorpro.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.utils.FileUtil;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class NuevoPost extends AppCompatActivity {
    CircleImageView mCircleImageViewBack;
    ImageView mImageViewPost;
    File mImageFile;
    private final int GALLERY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mImageViewPost = findViewById(R.id.subirImagen);
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