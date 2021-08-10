package com.escom.gestorpro.fragments;

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
import android.widget.Button;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.MainActivity;
import com.escom.gestorpro.activities.NuevoProyectoActivity;
import com.escom.gestorpro.activities.RegistroTarea;
import com.escom.gestorpro.adapters.TareaAdapter;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineaTiempoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineaTiempoFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    TareaProvider mTareaProvider;
    TareaAdapter mTareaAdapter;

    RecyclerView mRecyclerView;
    FloatingActionButton fab;


    public LineaTiempoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineaTiempoFragment.
     */
    public static LineaTiempoFragment newInstance(String param1, String param2) {
        LineaTiempoFragment fragment = new LineaTiempoFragment();
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
        View vista = inflater.inflate(R.layout.fragment_linea_tiempo, container, false);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mTareaProvider = new TareaProvider();

        mRecyclerView =  vista.findViewById(R.id.RecyclerViewTareas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);
        fab = vista.findViewById(R.id.newTarea);

        getRol(mAuthProvider.getUid());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTarea();
            }
        });

        return  vista;

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
                        if (rol.equals("Líder de proyecto")){
                            query[0] = mTareaProvider.getAllTarea();
                        }
                        else{
                            query[0] = mTareaProvider.getTareasByUser(mAuthProvider.getUid());
                        }
                        FirestoreRecyclerOptions<Tarea>  options = new FirestoreRecyclerOptions.Builder<Tarea>()
                                .setQuery(query[0], Tarea.class)
                                .build();

                        mTareaAdapter = new TareaAdapter(options, getActivity());
                        mRecyclerView.setAdapter(mTareaAdapter);
                        mTareaAdapter.startListening();
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mTareaAdapter.stopListening();
    }

    private void getRol(String uid) {
        mUserProvider.getUser(uid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.contains("rol")){
                    String rol = documentSnapshot.getString("rol");
                    if (rol.equals("Líder de proyecto")){
                        fab.setVisibility(View.VISIBLE);
                    }
                    else{
                        fab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void newTarea() {
        Intent miIntent = new Intent(getActivity(), RegistroTarea.class);
        startActivity(miIntent);
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