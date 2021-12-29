package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.activities.EstimacionDetailActivity;
import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Estimacion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EstimacionAdapter extends FirestoreRecyclerAdapter<Estimacion, EstimacionAdapter.ViewHolder>{
    Context context;

    public EstimacionAdapter(FirestoreRecyclerOptions<Estimacion> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Estimacion estimacion) {
        String nombre = estimacion.getNombre();
        double costo = estimacion.getCosto();
        String id = estimacion.getIdProyecto();

        holder.textViewNombre.setText(nombre);
        holder.textViewCosto.setText(String.valueOf(costo));

        holder.linearLayoutEstimacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EstimacionDetailActivity.class);
                intent.putExtra("idProyecto", id);
                context.startActivity(intent);
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
        LinearLayout linearLayoutEstimacion;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewNombre = view.findViewById(R.id.textViewNombre);
            textViewCosto = view.findViewById(R.id.textViewCosto);
            linearLayoutEstimacion = view.findViewById(R.id.linearLayoutEstimacion);

            viewHolder = view;
        }
    }
}
