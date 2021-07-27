package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputCel;
    Button mButtonRegister;
    Spinner spinnerRoles;
    AlertDialog mDialog;

    AuthProvider mAuthProvider;
    UserProvider mUsersProvider;

    String rol_seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        mTextInputUsername = findViewById(R.id.textInputUsername);
        mTextInputCel = findViewById(R.id.textInputCel);
        mButtonRegister = findViewById(R.id.btConfirm);
        spinnerRoles = (Spinner)findViewById(R.id.spinerRoles);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(adapter);

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        spinnerRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Comprobar que sea de la opcion 1 a 3
                rol_seleccionado = spinnerRoles.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rol_seleccionado = "Sin rol";
            }
        });

    }

    private void register() {
        String username = mTextInputUsername.getText().toString();
        String cel = mTextInputCel.getText().toString();

        if (!username.isEmpty()) {
            updateUser(username, cel, rol_seleccionado);
        }
        else {
            Toast.makeText(this, "Revisa los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUser (final String usuario, String cel, String rol){
        String id = mAuthProvider.getUid();
        Users user = new Users();
        user.setUsuario(usuario);
        user.setCelular(cel);
        user.setId(id);
        user.setRol(rol);
        mDialog.show();
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Intent miIntent = new Intent(CompleteProfileActivity.this, MenuActivity.class);
                    miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(miIntent);

                }
                else{
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}