package com.escom.gestorpro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResponsablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResponsablesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ArrayList<ResponsablesCardElement> listaResponsables;

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
    // TODO: Rename and change types and number of parameters
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
        listaResponsables = new ArrayList<>();
        recyclerView = (RecyclerView) vista.findViewById(R.id.RecyclerResponsable);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarlista();
        ResponsablesAdapter adapter = new ResponsablesAdapter(listaResponsables, getContext());
        recyclerView.setAdapter(adapter);

        return vista;
    }

    private void llenarlista() {

        listaResponsables.add(new ResponsablesCardElement("Andrea López Hernández"));
        listaResponsables.add(new ResponsablesCardElement("César Olivares Solano"));
        listaResponsables.add(new ResponsablesCardElement("Héctor García Estrada"));
        listaResponsables.add(new ResponsablesCardElement("Luis Hernández García"));
        listaResponsables.add(new ResponsablesCardElement("Rocío Lozornio Olivares"));
        listaResponsables.add(new ResponsablesCardElement("Sergio Lozornio Olivares"));

    }
}