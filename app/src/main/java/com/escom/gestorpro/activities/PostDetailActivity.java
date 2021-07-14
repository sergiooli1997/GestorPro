package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    PostProvider mPostProvider;
    UserProvider mUserProvider;

    String mExtraPostId;
    String mIdUser = "";

    TextView textViewUsuario;
    TextView textViewCel;
    TextView textViewDesc;
    ImageView imageViewPost;
    CircleImageView circleImageViewProfile;
    CircleImageView mCircleImageViewBack;
    Button btnVerPerfil;
    FloatingActionButton mFabComment;

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
        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
        mFabComment = findViewById(R.id.fabComment);

        getPost();

        mFabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("Comentario");
        alert.setMessage("Ingresa tu comentario");

        final EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Escribe un comentario");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
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
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void goToShowProfile() {
        if (!mIdUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("usuario", mIdUser);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_LONG).show();
        }

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
                        mIdUser = documentSnapshot.getString("usuario");
                        getUserInfo(mIdUser);
                    }

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