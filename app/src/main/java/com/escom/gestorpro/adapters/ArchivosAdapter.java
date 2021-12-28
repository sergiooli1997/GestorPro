package com.escom.gestorpro.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Archivo;
import com.escom.gestorpro.models.Riesgo;
import com.escom.gestorpro.providers.ArchivosProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArchivosAdapter extends FirestoreRecyclerAdapter<Archivo, ArchivosAdapter.ViewHolder>{
    Context context;
    ArchivosProvider mArchivosProvider;

    public ArchivosAdapter(FirestoreRecyclerOptions<Archivo> options, Context context){
        super(options);
        this.context = context;
        mArchivosProvider = new ArchivosProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Archivo archivo) {
        String nombre = archivo.getNombre();
        String descripcion = archivo.getDescripcion();
        String link = archivo.getLink();
        String id = archivo.getId();

        holder.textViewNombre.setText(nombre);
        holder.textViewDescripcion.setText(descripcion);

        holder.linearLayoutArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!link.isEmpty() && link!=null){
                    Uri uri = Uri.parse(link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
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
                .setTitle("Eliminar archivo")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteArchivo(id);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deleteArchivo(String id) {
        mArchivosProvider.deleteArchivo(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Archivo eliminado", Toast.LENGTH_SHORT).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_archivo, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewDescripcion;
        CircleImageView circleImageDelete;
        LinearLayout linearLayoutArchivo;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewNombre = view.findViewById(R.id.textViewNombre);
            textViewDescripcion = view.findViewById(R.id.textViewDescripcion);
            linearLayoutArchivo = view.findViewById(R.id.linearLayoutEmpresa);
            circleImageDelete = view.findViewById(R.id.circleImageDelete);

            viewHolder = view;
        }
    }
}
