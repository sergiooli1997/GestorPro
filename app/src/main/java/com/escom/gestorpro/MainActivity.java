package com.escom.gestorpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView textViewRegister;
    TextInputEditText txtInputEmail;
    TextInputEditText txtInputPassword;
    Button btnLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRegister = findViewById(R.id.textViewRegister);
        txtInputEmail = findViewById(R.id.txtUsuario);
        txtInputPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnIngresar);
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

        private void login() {
            String email = txtInputEmail.getText().toString();
            String password = txtInputPassword.getText().toString();
            Log.d("CAMPO", "email: " + email);
            Log.d("CAMPO", "password: " + password);
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent miIntent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(miIntent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "El email o la contraseña no son corrrectas", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
}