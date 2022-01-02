package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.providers.EstimacionProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;

public class EstimacionDetailActivity extends AppCompatActivity {
    String mExtraProyectoId;
    EstimacionProvider mEstimacionProvider;

    TextView textViewPREC;
    TextView textViewFLEX;
    TextView textViewRESL;
    TextView textViewTEAM;
    TextView textViewPMAT;
    TextView textViewSF;
    TextView textViewE;
    TextView textViewSize;
    TextView textViewPM;
    TextView textViewF;
    TextView textViewTDEV;
    TextView textViewSalario;
    TextView textViewOtrosGastos;
    TextView textViewCosto;
    ImageView imageViewBack;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimacion_detail);
        mEstimacionProvider = new EstimacionProvider();

        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        textViewPREC = findViewById(R.id.textViewPREC);
        textViewFLEX = findViewById(R.id.textViewFLEX);
        textViewRESL = findViewById(R.id.textViewRESL);
        textViewTEAM = findViewById(R.id.textViewTEAM);
        textViewPMAT = findViewById(R.id.textViewPMAT);
        textViewSF = findViewById(R.id.textViewSF);
        textViewE = findViewById(R.id.textViewE);
        textViewSize = findViewById(R.id.textViewSize);
        textViewPM = findViewById(R.id.textViewPM);
        textViewF = findViewById(R.id.textViewF);
        textViewTDEV = findViewById(R.id.textViewTDEV);
        textViewSalario = findViewById(R.id.textViewSalario);
        textViewOtrosGastos = findViewById(R.id.textViewOtrosGastos);
        textViewCosto = findViewById(R.id.textViewCosto);
        imageViewBack = findViewById(R.id.imageViewBack);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getEstimacion();
    }

    private void getEstimacion() {
        mEstimacionProvider.getEstimacionesByProyecto(mExtraProyectoId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        double prec_valor = document.getDouble("prec");
                        textViewPREC.setText(new DecimalFormat("0.00").format(prec_valor) + " + ");
                        double flex_valor = document.getDouble("flex");
                        textViewFLEX.setText(new DecimalFormat("0.00").format(flex_valor) + " + ");
                        double resl_valor = document.getDouble("resl");
                        textViewRESL.setText(new DecimalFormat("0.00").format(resl_valor) + " + ");
                        double team_valor = document.getDouble("team");
                        textViewTEAM.setText(new DecimalFormat("0.00").format(team_valor) + " + ");
                        double pmat_valor = document.getDouble("pmat");
                        textViewPMAT.setText(new DecimalFormat("0.00").format(pmat_valor));
                        double sf_valor = document.getDouble("sf");
                        textViewSF.setText(new DecimalFormat("0.00").format(sf_valor));
                        double e_valor = document.getDouble("e");
                        textViewE.setText(new DecimalFormat("0.00").format(e_valor));
                        double size_valor = document.getDouble("size");
                        textViewSize.setText(new DecimalFormat("0.00").format(size_valor));
                        double pm_valor = document.getDouble("pm");
                        textViewPM.setText(new DecimalFormat("0.00").format(pm_valor));
                        double f_valor = document.getDouble("f");
                        textViewF.setText(new DecimalFormat("0.00").format(f_valor));
                        double tdev_valor = document.getDouble("tdev");
                        textViewTDEV.setText(new DecimalFormat("0.00").format(tdev_valor));
                        double salario_valor = document.getDouble("salario");
                        textViewSalario.setText(new DecimalFormat("0,000.00").format(salario_valor));
                        double otros_gastos_valor = document.getDouble("otrosGastos");
                        textViewOtrosGastos.setText(" + " + new DecimalFormat("0.00").format(otros_gastos_valor));
                        double costo_valor = document.getDouble("costo");
                        if (costo_valor<1000000){
                            textViewCosto.setText("$ " + new DecimalFormat("0,000.00").format(costo_valor));
                        }
                        else{
                            textViewCosto.setText("$ " + new DecimalFormat("0,000,000.00").format(costo_valor));
                        }
                    }
                }
            }
        });
    }
}