package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.ProyectoDetailActivity;
import com.escom.gestorpro.activities.TareaDetailActivity;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.TareaProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TareaAdapter extends FirestoreRecyclerAdapter<Tarea, TareaAdapter.ViewHolder>{
    Context context;
    TareaProvider mTareaProvider;

    public TareaAdapter (FirestoreRecyclerOptions<Tarea> options, Context context){
        super(options);
        this.context = context;
        mTareaProvider = new TareaProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Tarea tarea) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String tareaId = document.getId();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        String titulo_tarea = tarea.getNombre();
        holder.textViewTitleTarea.setText(titulo_tarea);

        long timestamp_inicio = tarea.getFecha_inicio();
        Date date1 = new Date(timestamp_inicio);
        String fecha_inicio = formatter.format(date1);
        holder.textViewFechaInicio.setText("Fecha inicio: " + fecha_inicio);

        long timestamp_fin = tarea.getFecha_fin();
        Date date2 = new Date(timestamp_fin);
        String fecha_fin = formatter.format(date2);
        holder.textViewFechaFin.setText("Fecha fin: " + fecha_fin);

        /*String titulo_proyecto = tarea.getIdProyecto();
        holder.textViewTitleProyecto.setText(titulo_proyecto);*/

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TareaDetailActivity.class);
                intent.putExtra("id", tareaId);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tarea, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitleTarea;
        TextView textViewFechaInicio;
        TextView textViewFechaFin;
        TextView textViewTitleProyecto;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewTitleTarea = view.findViewById(R.id.textViewTitleTarea);
            textViewFechaInicio = view.findViewById(R.id.textViewFechaInicio);
            textViewFechaFin = view.findViewById(R.id.textViewFechaFin);
            textViewTitleProyecto = view.findViewById(R.id.textViewTitleProyecto);

            viewHolder = view;
        }
    }
}
