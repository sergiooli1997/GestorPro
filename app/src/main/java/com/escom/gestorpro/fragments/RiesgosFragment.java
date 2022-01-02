package com.escom.gestorpro.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.MainActivity;
import com.escom.gestorpro.adapters.ProyectosAdapter;
import com.escom.gestorpro.adapters.RiesgosAdapter;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RiesgosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RiesgosFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ProyectoProvider mProyectosProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;

    RiesgosAdapter mProyectosAdapter;

    RecyclerView mRecyclerProyectos;
    AlertDialog mDialog;
    Button btnRiesgos;

    public RiesgosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RiesgosFragment.
     */
    public static RiesgosFragment newInstance(String param1, String param2) {
        RiesgosFragment fragment = new RiesgosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_riesgos, container, false);
        mProyectosProvider = new ProyectoProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        mDialog.show();

        btnRiesgos = vista.findViewById(R.id.btnRiesgosLink);

        btnRiesgos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://blogs.portafolio.co/buenas-practicas-de-auditoria-y-control-interno-en-las-organizaciones/disenar-una-matriz-riesgos/";
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        mRecyclerProyectos =  vista.findViewById(R.id.RecyclerViewProyectos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerProyectos.setLayoutManager(linearLayoutManager);

        setHasOptionsMenu(true);
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Query[] query = new Query[1];
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("rol")){
                        String rol = documentSnapshot.getString("rol");
                        if (rol.equals("Cliente")){
                            query[0] = mProyectosProvider.getProyectoByCliente(mAuthProvider.getUid());
                        }
                        else{
                            query[0] = mProyectosProvider.getProyectoByUser(mAuthProvider.getUid());
                        }
                        FirestoreRecyclerOptions<Proyecto> options = new FirestoreRecyclerOptions.Builder<Proyecto>()
                                .setQuery(query[0], Proyecto.class)
                                .build();
                        mProyectosAdapter = new RiesgosAdapter(options, getActivity());
                        mRecyclerProyectos.setAdapter(mProyectosAdapter);
                        mProyectosAdapter.startListening();

                        mDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mProyectosAdapter.stopListening();
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