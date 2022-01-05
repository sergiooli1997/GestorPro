package com.escom.gestorpro.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.escom.gestorpro.activities.EstimacionActivity;
import com.escom.gestorpro.activities.MiEmpresaActivity;
import com.escom.gestorpro.activities.BuzonActivity;
import com.escom.gestorpro.activities.DatosAnalisisActivity;
import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.MainActivity;
import com.escom.gestorpro.providers.AuthProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstrategiasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstrategiasFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String url = "";
    Button mButtonBuenasPracticas;
    Button mButtonEstrategias;
    Button mButtonConceptos;
    Button mButtonAnalisisDatos;
    Button mButtonBuzon;
    Button mButtonMiEmpresa;
    Button mButtonEstimacion;
    Button mButtonISO;

    AuthProvider mAuthProvider;

    public EstrategiasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstrategiasFragment.
     */
    public static EstrategiasFragment newInstance(String param1, String param2) {
        EstrategiasFragment fragment = new EstrategiasFragment();
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
        View view = inflater.inflate(R.layout.fragment_estrategias, container, false);

        mAuthProvider = new AuthProvider();

        mButtonBuenasPracticas = view.findViewById(R.id.btnBuenasPracticas);
        mButtonAnalisisDatos = view.findViewById(R.id.btnAnalisisDatos);
        mButtonEstrategias = view.findViewById(R.id.btnEstrategias);
        mButtonConceptos = view.findViewById(R.id.btnConceptosFund);
        mButtonBuzon = view.findViewById(R.id.btnBuzon);
        mButtonMiEmpresa = view.findViewById(R.id.btnMiEmpresa);
        mButtonEstimacion = view.findViewById(R.id.btnEstimacionCosto);
        mButtonISO = view.findViewById(R.id.btnISO);

        mButtonBuenasPracticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBuenasPracticas();
            }
        });

        mButtonAnalisisDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAnalisisDatos();
            }
        });

        mButtonEstrategias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEstrategias();
            }
        });

        mButtonConceptos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToConceptos();
            }
        });

        mButtonBuzon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuzonActivity.class);
                startActivity(intent);
            }
        });
        mButtonMiEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MiEmpresaActivity.class);
                startActivity(intent);
            }
        });
        mButtonEstimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EstimacionActivity.class);
                startActivity(intent);
            }
        });

        mButtonISO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToISO();
            }
        });
        setHasOptionsMenu(true);

        return view;
    }

    private void goToISO() {
        String link = "https://www.iso.org/obp/ui/#iso:std:iso:9001:ed-5:v1:es";
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void goToConceptos() {
        String link = "https://www.chaparral-tolima.gov.co/NuestraAlcaldia/SaladePrensa/PublishingImages/Paginas/autocapacitaciones-talento-humano-tic-gel-alcaldia-chaparral-tolima/material%202%20Hardware%20y%20Software.pdf";
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void goToEstrategias() {
        String link = "http://ipneconomybook.herokuapp.com/contenidos/u2";
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void goToAnalisisDatos() {
        Intent intent = new Intent(getActivity(), DatosAnalisisActivity.class);
        startActivity(intent);
    }

    private void goToBuenasPracticas() {
        String link = "https://samuelcasanova.com/2016/09/resumen-clean-code/";
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
}