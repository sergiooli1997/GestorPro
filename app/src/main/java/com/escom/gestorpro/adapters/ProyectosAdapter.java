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
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.models.Proyecto;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.LikesProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.ProyectoProvider;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProyectosAdapter extends FirestoreRecyclerAdapter<Proyecto, ProyectosAdapter.ViewHolder> {
    Context context;
    ProyectoProvider mProyectoProvider;

    public ProyectosAdapter(FirestoreRecyclerOptions<Proyecto> options, Context context) {
        super(options);
        this.context = context;
        mProyectoProvider = new ProyectoProvider();

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Proyecto proyecto) {
        String titulo = proyecto.getNombre();
        holder.textViewTitleProyecto.setText(titulo);

        String fecha_inicio = RelativeTime.getTimeAgo(proyecto.getFecha_inicio(), context);
        holder.textViewFechaInicio.setText(fecha_inicio);

        String fecha_fin = RelativeTime.getTimeAgo(proyecto.getFecha_fin(), context);
        holder.textViewFechaInicio.setText(fecha_fin);

        holder.textViewAvance.setText("0 % de avance");

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, NuevoProyectoActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);*/
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
