package com.escom.gestorpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputCel;
    Button mButtonRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        mTextInputUsername = findViewById(R.id.textInputUsername);
        mTextInputCel = findViewById(R.id.textInputCel);
        mButtonRegister = findViewById(R.id.btConfirm);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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
        String id = mAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("usuario", usuario);
        map.put("celular", cel);
        mFirestore.collection("Usuarios").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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