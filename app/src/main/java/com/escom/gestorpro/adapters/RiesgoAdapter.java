package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.TareaDetailActivity;
import com.escom.gestorpro.models.Riesgo;
import com.escom.gestorpro.models.Tarea;
import com.escom.gestorpro.providers.RiesgosProvider;
import com.escom.gestorpro.providers.TareaProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        LinearLayout linearLayoutRiesgo;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewProbabilidad = view.findViewById(R.id.textViewProbabilidad);
            textViewImpacto = view.findViewById(R.id.textViewImpacto);
            textViewNombre = view.findViewById(R.id.textViewNombre);
            linearLayoutRiesgo = view.findViewById(R.id.linearLayoutRiesgo);
            viewColor = view.findViewById(R.id.viewColor);

            viewHolder = view;
        }
    }
}
