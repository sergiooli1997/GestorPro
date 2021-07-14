package com.escom.gestorpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {


    LinearLayout mLinearLayoutEditProfile;
    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageProfile;
    CircleImageView mCircleImageViewBack;

    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mLinearLayoutEditProfile = findViewById(R.id.linearLayoutEditProfile);
        mTextViewEmail = findViewById(R.id.textViewEmail);
        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewPhone = findViewById(R.id.textViewphone);
        mTextViewPostNumber = findViewById(R.id.textViewPostNumber);
        mCircleImageProfile = findViewById(R.id.circleImageProfile);
        mCircleImageViewBack = findViewById(R.id.circleImageBack);
        mImageViewCover = findViewById(R.id.imageViewCover);

        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        mExtraIdUser = getIntent().getStringExtra("usuario");

        getUser();
        getPostNumber();

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getPostNumber() {
        mPostProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }

    private void getUser() {
        mUsersProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("email")) {
                        String email = documentSnapshot.getString("email");
                        mTextViewEmail.setText(email);
                    }
                    if (documentSnapshot.contains("celular")) {
                        String phone = documentSnapshot.getString("celular");
                        mTextViewPhone.setText(phone);
                    }
                    if (documentSnapshot.contains("usuario")) {
                        String username = documentSnapshot.getString("usuario");
                        mTextViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(imageProfile).into(mCircleImageProfile);
                            }
                        }
                    }
                    if (documentSnapshot.contains("imageCover")) {
                        String imageCover = documentSnapshot.getString("imageCover");
                        if (imageCover != null) {
                            if (!imageCover.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(imageCover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }
        });
    }
}