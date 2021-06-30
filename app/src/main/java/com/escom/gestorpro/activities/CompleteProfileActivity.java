package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Users;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputCel;
    Button mButtonRegister;
    AuthProvider mAuthProvider;
    UserProvider mUsersProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        mTextInputUsername = findViewById(R.id.textInputUsername);
        mTextInputCel = findViewById(R.id.textInputCel);
        mButtonRegister = findViewById(R.id.btConfirm);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        String cel = mTextInputCel.getText().toString();

        if (!username.isEmpty()) {
            updateUser(username, cel);
        }
        else {
            Toast.makeText(this, "Revisa los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser (final String usuario, String cel){
        String id = mAuthProvider.getUid();
        Users user = new Users();
        user.setUsuario(usuario);
        user.setCelular(cel);
        user.setId(id);
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent miIntent = new Intent(CompleteProfileActivity.this, MenuActivity.class);
                    startActivity(miIntent);

                }
                else{
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}