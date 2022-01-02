package com.escom.gestorpro.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.escom.gestorpro.models.Riesgo;
import com.escom.gestorpro.providers.RiesgosProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class RiesgoAdapter extends FirestoreRecyclerAdapter<Riesgo, RiesgoAdapter.ViewHolder>{
    Context context;
    RiesgosProvider mRiesgosProvider;

    public RiesgoAdapter(FirestoreRecyclerOptions<Riesgo> options, Context context){
        super(options);
        this.context = context;
        mRiesgosProvider = new RiesgosProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Riesgo riesgo) {
        String probabilidad = riesgo.getProbabilidad();
        String impacto = riesgo.getImpacto();
        String clasificacion = riesgo.getClasificacion();
        String nombre = riesgo.getNombre();
        String link = riesgo.getLink();
        String id = riesgo.getId();

        holder.textViewProbabilidad.setText(probabilidad);
        holder.textViewImpacto.setText(impacto);
        holder.textViewNombre.setText(nombre);

        if (clasificacion.equals("Alto")){
            holder.viewColor.setBackgroundColor(Color.RED);
        }
        else if (clasificacion.equals("Medio")){
            holder.viewColor.setBackgroundColor(Color.YELLOW);
        }
        else{
            holder.viewColor.setBackgroundColor(Color.GREEN);
        }

        holder.linearLayoutRiesgo.setOnClickListener(new View.OnClickListener() {
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
                .setTitle("Eliminar riesgo")
                .setMessage("¿Estas seguro de realizar esta acción?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRiesgo(id);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deleteRiesgo(String id) {
        mRiesgosProvider.delete(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Riesgo eliminado", Toast.LENGTH_LONG).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_riesgo, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View viewColor;
        TextView textViewNombre;
        TextView textViewProbabilidad;
        TextView textViewImpacto;
        CircleImageView circleImageDelete;
        LinearLayout linearLayoutRiesgo;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewProbabilidad = view.findViewById(R.id.textViewProbabilidad);
            textViewImpacto = view.findViewById(R.id.textViewImpacto);
            textViewNombre = view.findViewById(R.id.textViewNombre);
            circleImageDelete = view.findViewById(R.id.circleImageDelete);
            linearLayoutRiesgo = view.findViewById(R.id.linearLayoutRiesgo);
            viewColor = view.findViewById(R.id.viewColor);

            viewHolder = view;
        }
    }
}
