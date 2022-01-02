package com.escom.gestorpro.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.activities.EstimacionDetailActivity;
import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Estimacion;
import com.escom.gestorpro.providers.EstimacionProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class EstimacionAdapter extends FirestoreRecyclerAdapter<Estimacion, EstimacionAdapter.ViewHolder>{
    Context context;
    EstimacionProvider mEstimacionProvider;

    public EstimacionAdapter(FirestoreRecyclerOptions<Estimacion> options, Context context){
        super(options);
        this.context = context;
        mEstimacionProvider = new EstimacionProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Estimacion estimacion) {
        String nombre = estimacion.getNombre();
        double costo = estimacion.getCosto();
        String idProyecto = estimacion.getIdProyecto();
        String id = estimacion.getId();

        holder.textViewNombre.setText(nombre);
        if (costo < 1000000){
            holder.textViewCosto.setText("$ "+ new DecimalFormat("0,000.00").format(costo));
        }
        else{
            holder.textViewCosto.setText("$ " + new DecimalFormat("0,000,000.00").format(costo));
        }
        holder.linearLayoutEstimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EstimacionDetailActivity.class);
                intent.putExtra("idProyecto", idProyecto);
                context.startActivity(intent);
            }
        });

        holder.circleImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(id);
            }
        });
    }

    private void showConfirmDelete(String id) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar riesgo")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEstimacion(id);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deleteEstimacion(String id) {
        mEstimacionProvider.deleteEstimacion(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Estimación eliminada", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_estimacion, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewCosto;
        CircleImageView circleImageDelete;
        LinearLayout linearLayoutEstimacion;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewNombre = view.findViewById(R.id.textViewNombre);
            textViewCosto = view.findViewById(R.id.textViewCosto);
            circleImageDelete = view.findViewById(R.id.circleImageDelete);
            linearLayoutEstimacion = view.findViewById(R.id.linearLayoutEstimacion);

            viewHolder = view;
        }
    }
}
