package com.escom.gestorpro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView recyclerView;
    ArrayList<CardElement> listaPost;
    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
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
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        listaPost = new ArrayList<>();
        recyclerView = (RecyclerView) vista.findViewById(R.id.RecyclerPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarlista();
        PostAdapter adapter = new PostAdapter(listaPost, getContext());
        recyclerView.setAdapter(adapter);

        return vista;
    }

    private void llenarlista() {
        listaPost.add(new CardElement("Sergio", "13:30", "Primer día. ¡Qué emoción!"));
        listaPost.add(new CardElement("Rocio", "12:55", "Ánimo equipo."));
        listaPost.add(new CardElement("Norma", "11:34", "Hoy en la reunión hubo muchas ideas."));
        listaPost.add(new CardElement("Lola", "11:30", "Excelente trabajo. Nos felicitaron."));

    }
}