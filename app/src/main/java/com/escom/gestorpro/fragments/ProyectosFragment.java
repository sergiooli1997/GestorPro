package com.escom.gestorpro.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.MainActivity;
import com.escom.gestorpro.activities.NuevoProyectoActivity;
import com.escom.gestorpro.activities.ProyectoDetailActivity;
import com.escom.gestorpro.adapters.ProyectosAdapter;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProyectosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProyectosFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView mRecyclerProyectosCompletos;
    RecyclerView mRecyclerProyectosIncompletos;
    AlertDialog mDialog;

    UserProvider mUserProvider;
    ProyectosAdapter mProyectosIncompletosAdapter;
    ProyectosAdapter mProyectosCompletosAdapter;
    ProyectoProvider mProyectosProvider;
    AuthProvider mAuthProvider;

    public ProyectosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProyectosFragment.
     */
    public static ProyectosFragment newInstance(String param1, String param2) {
        ProyectosFragment fragment = new ProyectosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProyectosProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        View vista = inflater.inflate(R.layout.fragment_proyectos, container, false);
        FloatingActionButton fab = vista.findViewById(R.id.newProyecto);

        mDialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mDialog.show();

        mRecyclerProyectosCompletos =  vista.findViewById(R.id.RecyclerViewProyectosCompletos);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        mRecyclerProyectosCompletos.setLayoutManager(linearLayoutManager1);

        mRecyclerProyectosIncompletos =  vista.findViewById(R.id.RecyclerViewProyectosIncompletos);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        mRecyclerProyectosIncompletos.setLayoutManager(linearLayoutManager2);

        setHasOptionsMenu(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRol(mAuthProvider.getUid());
            }
        });

        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Query[] query = new Query[2];
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("rol")){
                        String rol = documentSnapshot.getString("rol");
                        if (rol.equals("Cliente")){
                            query[0] = mProyectosProvider.getProyectoCompletoByCliente(mAuthProvider.getUid(), 0);
                            query[1] = mProyectosProvider.getProyectoCompletoByCliente(mAuthProvider.getUid(), 1);
                        }
                        else{
                            query[0] = mProyectosProvider.getProyectoCompletoByUser(mAuthProvider.getUid(), 0);
                            query[1] = mProyectosProvider.getProyectoCompletoByUser(mAuthProvider.getUid(), 1);
                        }
                        FirestoreRecyclerOptions<Proyecto> options1 = new FirestoreRecyclerOptions.Builder<Proyecto>()
                                .setQuery(query[0], Proyecto.class)
                                .build();
                        FirestoreRecyclerOptions<Proyecto> options2 = new FirestoreRecyclerOptions.Builder<Proyecto>()
                                .setQuery(query[1], Proyecto.class)
                                .build();
                        mProyectosIncompletosAdapter = new ProyectosAdapter(options1, getActivity());
                        mRecyclerProyectosIncompletos.setAdapter(mProyectosIncompletosAdapter);
                        mProyectosIncompletosAdapter.startListening();

                        mProyectosCompletosAdapter = new ProyectosAdapter(options2, getActivity());
                        mRecyclerProyectosCompletos.setAdapter(mProyectosCompletosAdapter);
                        mProyectosCompletosAdapter.startListening();
                        mDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mProyectosIncompletosAdapter.stopListening();
        mProyectosCompletosAdapter.stopListening();
    }

    private void getRol(String uid) {
        mUserProvider.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if (rol.equals("Líder de proyecto")){
                        Intent intent = new Intent(getActivity(), NuevoProyectoActivity.class);
                        startActivity(intent);
                    }
                    else{
                        showDialogNuevoProyecto();
                    }
                }
            }
        });
    }

    private void showDialogNuevoProyecto() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Unete a un equipo");
        alert.setMessage("Ingresa el codigo para unirte a un proyecto");

        final EditText editText = new EditText(getActivity());
        editText.setHint("Escribe un codigo");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(getActivity());
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
                if (!value.isEmpty()){
                    mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                if(documentSnapshot.contains("rol")){
                                    String rol = documentSnapshot.getString("rol");
                                    if(rol.equals("Miembro del equipo")){
                                        unirProyecto(value);
                                    }
                                    else{
                                        unirCliente(value);
                                    }
                                }
                            }
                        }
                    });


                }
                else{
                    Toast.makeText(getActivity(), "Debe ingresar codigo", Toast.LENGTH_LONG).show();
                }

            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void unirCliente(String codigo) {
        mDialog.show();
        final String[] idProyecto = new String[1];
        mProyectosProvider.getIdByCode(codigo).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            idProyecto[0] = document.get("id").toString();
                            mProyectosProvider.updateCliente(idProyecto[0],mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProyectosProvider.getProyectoById(idProyecto[0]).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                if (documentSnapshot.contains("nombre")){
                                                    String nombre = documentSnapshot.getString("nombre");
                                                    Intent intent = new Intent(getActivity(), ProyectoDetailActivity.class);
                                                    intent.putExtra("id", idProyecto[0]);
                                                    startActivity(intent);
                                                    Toast.makeText(getActivity(), "Te uniste al proyecto " + nombre, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Proyecto inexsistente", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "No existe el código", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void unirProyecto(String codigo) {
        mDialog.show();
        final String[] idProyecto = new String[1];
        mProyectosProvider.getIdByCode(codigo).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            idProyecto[0] = document.get("id").toString();
                            mProyectosProvider.addUser(idProyecto[0], mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProyectosProvider.getProyectoById(idProyecto[0]).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                if (documentSnapshot.contains("nombre")){
                                                    String nombre = documentSnapshot.getString("nombre");
                                                    Intent intent = new Intent(getActivity(), ProyectoDetailActivity.class);
                                                    intent.putExtra("id", idProyecto[0]);
                                                    startActivity(intent);
                                                    Toast.makeText(getActivity(), "Te uniste al proyecto " + nombre, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Proyecto inexsistente", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(getActivity(), "No existe el código", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);

    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}