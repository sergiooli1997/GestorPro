package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    PostProvider mPostProvider;
    UserProvider mUserProvider;

    String mExtraPostId;

    TextView textViewUsuario;
    TextView textViewCel;
    TextView textViewDesc;
    ImageView imageViewPost;
    CircleImageView circleImageViewProfile;
    Button btnVerPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();

        mExtraPostId = getIntent().getStringExtra("id");

        textViewUsuario = findViewById(R.id.textViewUsuarioPD);
        textViewCel = findViewById(R.id.textViewCelPD);
        textViewDesc = findViewById(R.id.textViewDescPD);
        imageViewPost = findViewById(R.id.imageViewPD);
        circleImageViewProfile = findViewById(R.id.circleImageProfileDetail);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);

        getPost();
    }

    private void getPost() {
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    if (documentSnapshot.contains("image")) {
                        String image1 = documentSnapshot.getString("image");
                        Picasso.with(PostDetailActivity.this).load(image1).into(imageViewPost);
                    }
                    if (documentSnapshot.contains("texto")) {
                        String texto = documentSnapshot.getString("texto");
                        textViewDesc.setText(texto);
                    }
                    if (documentSnapshot.contains("usuario")) {
                        String idUser = documentSnapshot.getString("usuario");
                        getUserInfo(idUser);
                    }|

                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("usuario")) {
                        String username = documentSnapshot.getString("usuario");
                        textViewUsuario.setText(username);
                    }
                    if (documentSnapshot.contains("celular")) {
                        String phone = documentSnapshot.getString("celular");
                        textViewCel.setText(phone);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        Picasso.with(PostDetailActivity.this).load(imageProfile).into(circleImageViewProfile);
                    }
                }
            }
        });
    }
}