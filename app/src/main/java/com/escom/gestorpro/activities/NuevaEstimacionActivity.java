package com.escom.gestorpro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Estimacion;
import com.escom.gestorpro.providers.EstimacionProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class NuevaEstimacionActivity extends AppCompatActivity {
    String mExtraProyectoId;
    String prec;
    String flex;
    String resl;
    String team;
    String pmat;
    EstimacionProvider mEstimacionProvider;

    TextInputEditText textInputNombre;
    TextInputEditText textInputSize;
    TextInputEditText textInputSalario;
    TextInputEditText textInputOtrosGastos;
    Spinner spinnerPREC;
    Spinner spinnerFLEX;
    Spinner spinnerRESL;
    Spinner spinnerTEAM;
    Spinner spinnerPMAT;
    Button btnAceptar;
    ImageView mImageViewBack;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_estimacion);
        mEstimacionProvider = new EstimacionProvider();

        textInputNombre = findViewById(R.id.textInputNombre);
        textInputSize = findViewById(R.id.textInputSize);
        textInputSalario = findViewById(R.id.textInputSalario);
        textInputOtrosGastos = findViewById(R.id.textInputOtrosGastos);
        spinnerPREC = (Spinner)findViewById(R.id.spinerPREC);
        spinnerFLEX = (Spinner)findViewById(R.id.spinerFLEX);
        spinnerRESL = (Spinner)findViewById(R.id.spinerRESL);
        spinnerTEAM = (Spinner)findViewById(R.id.spinerTEAM);
        spinnerPMAT = (Spinner)findViewById(R.id.spinerPMAT);
        btnAceptar = findViewById(R.id.btnAceptar);
        mImageViewBack = findViewById(R.id.imageViewBack);

        mExtraProyectoId = getIntent().getStringExtra("idProyecto");

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        ArrayAdapter<CharSequence> adapter_prec = ArrayAdapter.createFromResource(this,
                R.array.prec, android.R.layout.simple_spinner_item);
        adapter_prec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPREC.setAdapter(adapter_prec);

        ArrayAdapter<CharSequence> adapter_flex = ArrayAdapter.createFromResource(this,
                R.array.flex, android.R.layout.simple_spinner_item);
        adapter_flex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFLEX.setAdapter(adapter_flex);

        ArrayAdapter<CharSequence> adapter_resl = ArrayAdapter.createFromResource(this,
                R.array.resl, android.R.layout.simple_spinner_item);
        adapter_resl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRESL.setAdapter(adapter_resl);

        ArrayAdapter<CharSequence> adapter_team = ArrayAdapter.createFromResource(this,
                R.array.team, android.R.layout.simple_spinner_item);
        adapter_team.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTEAM.setAdapter(adapter_team);

        ArrayAdapter<CharSequence> adapter_pmat = ArrayAdapter.createFromResource(this,
                R.array.pmat, android.R.layout.simple_spinner_item);
        adapter_pmat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPMAT.setAdapter(adapter_pmat);

        spinnerPREC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerPREC.getSelectedItem().toString().equals("Seleccione precedencia")){
                    prec = "Muy baja";
                }
                else{
                    prec = spinnerPREC.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFLEX.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerFLEX.getSelectedItem().toString().equals("Seleccione flexibilidad")){
                    flex = "Muy baja";
                }
                else{
                    flex = spinnerFLEX.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerRESL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerRESL.getSelectedItem().toString().equals("Seleccione resolución")){
                    resl = "Muy baja";
                }
                else{
                    resl = spinnerRESL.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTEAM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTEAM.getSelectedItem().toString().equals("Seleccione cohesión del equipo")){
                    team = "Muy baja";
                }
                else{
                    team = spinnerTEAM.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPMAT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerPMAT.getSelectedItem().toString().equals("Seleccione madurez del proceso")){
                    pmat = "Muy baja";
                }
                else{
                    pmat = spinnerPMAT.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearEstimacion();
            }
        });

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void crearEstimacion() {
        Estimacion estimacion = new Estimacion();
        estimacion.setNombre(textInputNombre.getText().toString());
        estimacion.setIdProyecto(mExtraProyectoId);
        double size_valor = Double.parseDouble(textInputSize.getText().toString());
        estimacion.setSize(size_valor);
        double salario_valor = Double.parseDouble(textInputSalario.getText().toString());
        estimacion.setSalario(salario_valor);
        double otros_gastos_valor = Double.parseDouble(textInputOtrosGastos.getText().toString());
        estimacion.setOtrosGastos(otros_gastos_valor);
        double prec_valor = calcularPrec(prec);
        double flex_valor = calcularFlex(flex);
        double resl_valor = calcularResl(resl);
        double team_valor = calcularTeam(team);
        double pmat_valor = calcularPmat(pmat);
        estimacion.setPREC(prec_valor);
        estimacion.setFLEX(flex_valor);
        estimacion.setRESL(resl_valor);
        estimacion.setTEAM(team_valor);
        estimacion.setPMAT(pmat_valor);
        double sf_valor = prec_valor + flex_valor + resl_valor + team_valor + pmat_valor;
        estimacion.setSF(sf_valor);
        double e_valor = 0.91 + (0.01 * sf_valor);
        estimacion.setE(e_valor);
        double pm_valor = 2.94 * Math.pow(size_valor, e_valor);
        estimacion.setPM(pm_valor);
        double f_valor = 0.28 + 0.2 * 0.01 * sf_valor;
        estimacion.setF(f_valor);
        double tdev_valor = 3.67 * Math.pow(pm_valor, f_valor);
        estimacion.setTDEV(tdev_valor);
        double costo = (pm_valor * tdev_valor * salario_valor) + otros_gastos_valor;
        estimacion.setCosto(costo);

        mEstimacionProvider.save(estimacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NuevaEstimacionActivity.this, "Estimación calculada", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(NuevaEstimacionActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private double calcularPmat(String pmat) {
        switch (pmat) {
            case "Muy baja":
                return 7.80;
            case "Baja":
                return 6.24;
            case "Nominal":
                return 4.68;
            case "Alta":
                return 3.12;
            case "Muy alta":
                return 1.56;
            default:
                return 0;
        }
    }

    private double calcularTeam(String team) {
        switch (team) {
            case "Muy baja":
                return 5.48;
            case "Baja":
                return 4.38;
            case "Nominal":
                return 3.29;
            case "Alta":
                return 2.19;
            case "Muy alta":
                return 1.10;
            default:
                return 0;
        }
    }

    private double calcularResl(String resl) {
        switch (resl) {
            case "Muy baja":
                return 7.07;
            case "Baja":
                return 5.65;
            case "Nominal":
                return 4.24;
            case "Alta":
                return 2.83;
            case "Muy alta":
                return 1.41;
            default:
                return 0;
        }
    }

    private double calcularFlex(String flex) {
        switch (flex) {
            case "Muy baja":
                return 5.07;
            case "Baja":
                return 4.05;
            case "Nominal":
                return 3.04;
            case "Alta":
                return 2.03;
            case "Muy alta":
                return 1.01;
            default:
                return 0;
        }
    }

    private double calcularPrec(String prec) {
        switch (prec) {
            case "Muy baja":
                return 6.20;
            case "Baja":
                return 4.96;
            case "Nominal":
                return 3.72;
            case "Alta":
                return 2.48;
            case "Muy alta":
                return 1.24;
            default:
                return 0;
        }
    }
}