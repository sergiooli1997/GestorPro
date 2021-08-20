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
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.MainActivity;
import com.escom.gestorpro.adapters.ResponsablesAdapter;
import com.escom.gestorpro.models.Users;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResponsablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResponsablesFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener{

    // TODO: Filtrar miembros por proyecto en spinner
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView mRecyclerView;
    ResponsablesAdapter mResponsablesAdapter;
    ResponsablesAdapter mResponsablesAdapterSearch;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    MaterialSearchBar mSearchBar;

    public ResponsablesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResponsablesFragment.
     */
    public static ResponsablesFragment newInstance(String param1, String param2) {
        ResponsablesFragment fragment = new ResponsablesFragment();
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
        View vista = inflater.inflate(R.layout.fragment_responsables, container, false);

        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        mSearchBar = vista.findViewById(R.id.searchBar);

        mRecyclerView = vista.findViewById(R.id.RecyclerResponsable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        setHasOptionsMenu(true);

        mSearchBar.setOnSearchActionListener(this);

        return vista;
    }

    private void searchByName(String name){
        Query query = mUserProvider.getUserByName(name);
        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        mResponsablesAdapterSearch = new ResponsablesAdapter(options, getContext());
        mResponsablesAdapterSearch.notifyDataSetChanged();
        mRecyclerView.setAdapter(mResponsablesAdapterSearch);
        mResponsablesAdapterSearch.startListening();
    }

    private void getAllUser(){
        Query query = mUserProvider.getAllUser();
        FirestoreRecyclerOptions<Users> options = new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();

        mResponsablesAdapter = new ResponsablesAdapter(options, getContext());
        mResponsablesAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mResponsablesAdapter);
        mResponsablesAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        mResponsablesAdapter.stopListening();
        if (mResponsablesAdapterSearch != null){
            mResponsablesAdapterSearch.stopListening();
        }
    }

    @Override
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

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllUser();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByName(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}