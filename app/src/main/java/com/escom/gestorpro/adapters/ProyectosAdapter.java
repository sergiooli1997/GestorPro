package com.escom.gestorpro.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.NuevoProyectoActivity;
import com.escom.gestorpro.activities.PostDetailActivity;
import com.escom.gestorpro.activities.ProyectoDetailActivity;
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.LikesProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.escom.gestorpro.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProyectosAdapter extends FirestoreRecyclerAdapter<Proyecto, ProyectosAdapter.ViewHolder> {
    Context context;
    ProyectoProvider mProyectoProvider;
    TareaProvider mTareaProvider;
    int total_tareas = 0;

    public ProyectosAdapter(FirestoreRecyclerOptions<Proyecto> options, Context context) {
        super(options);
        this.context = context;
        mProyectoProvider = new ProyectoProvider();
        mTareaProvider =  new TareaProvider();

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Proyecto proyecto) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String proyectoId = document.getId();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        String titulo = proyecto.getNombre();
        holder.textViewTitleProyecto.setText(titulo);

        long timestamp_inicio = proyecto.getFecha_inicio();
        Date date1 = new Date(timestamp_inicio);
        String fecha_inicio = formatter.format(date1);
        holder.textViewFechaInicio.setText("Fecha inicio: " + fecha_inicio);

        long timestamp_fin = proyecto.getFecha_fin();
        Date date2 = new Date(timestamp_fin);
        String fecha_fin = formatter.format(date2);
        holder.textViewFechaFin.setText("Fecha fin: " + fecha_fin);

        avance(proyectoId, holder);

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProyectoDetailActivity.class);
                intent.putExtra("id", proyectoId);
                context.startActivity(intent);
            }
        });


    }

    private void avance(String proyectoId, ViewHolder holder) {
        getNumberTareas(proyectoId);
        mTareaProvider.getTareaCompletoByProyecto(proyectoId, 1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    int tareas_completadas = queryDocumentSnapshots.size();
                    if (total_tareas != 0){
                        double avance = (tareas_completadas*100.00)/total_tareas;
                        holder.textViewAvance.setText(avance + "% de avance");
                    }
                    else{
                        holder.textViewAvance.setText("No hay tareas para calcular avance");
                    }
                }
            }
        });
    }

    private void getNumberTareas(String proyectoId) {
        mTareaProvider.getTareasTotalByProyecto(proyectoId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    total_tareas = queryDocumentSnapshots.size();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_proyecto, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitleProyecto;
        TextView textViewFechaInicio;
        TextView textViewFechaFin;
        TextView textViewAvance;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewTitleProyecto = view.findViewById(R.id.textViewTitleProyecto);
            textViewFechaInicio = view.findViewById(R.id.textViewFechaInicio);
            textViewFechaFin = view.findViewById(R.id.textViewFechaFin);
            textViewAvance = view.findViewById(R.id.textViewAvance);

            viewHolder = view;
        }
    }

}
