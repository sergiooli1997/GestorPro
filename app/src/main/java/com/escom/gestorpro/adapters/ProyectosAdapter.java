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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProyectosAdapter extends FirestoreRecyclerAdapter<Proyecto, ProyectosAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    ProyectoProvider mProyectoProvider;

    public ProyectosAdapter(FirestoreRecyclerOptions<Proyecto> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mProyectoProvider = new ProyectoProvider();

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Proyecto proyecto) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();

        String fecha_inicio = RelativeTime.getTimeAgo(proyecto.getFecha_inicio(), context);
        holder.textViewFechaInicio.setText(fecha_inicio);

        String fecha_fin = RelativeTime.getTimeAgo(proyecto.getFecha_fin(), context);
        holder.textViewFechaInicio.setText(fecha_fin);

        /*if(post.getUsuario().equals(mAuthProvider.getUid())){
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }
        else{
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (post.getImage() != null) {
            if (!post.getImage().isEmpty()) {
                Picasso.with(context).load(post.getImage()).into(holder.circleImageViewMyPost);
            }
        }*/

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NuevoProyectoActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDelete(postId);
            }
        });
    }

    private void showConfirmDelete(String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar publicación")
                .setMessage("¿¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deletePost(String postId) {
       /* mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "El post se elimino correctamente", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "No se pudo eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
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
        ImageView imageViewDelete;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewTitleProyecto = view.findViewById(R.id.textViewTitleProyecto);
            textViewFechaInicio = view.findViewById(R.id.textViewFechaInicio);
            textViewFechaFin = view.findViewById(R.id.textViewFechaFin);
            textViewAvance = view.findViewById(R.id.textViewAvance);
            imageViewDelete = view.findViewById(R.id.imageViewDeleteProyecto);

            viewHolder = view;
        }
    }

}
