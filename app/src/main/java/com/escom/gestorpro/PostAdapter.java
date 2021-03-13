package com.escom.gestorpro;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<CardElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public PostAdapter(List<CardElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.posts, null);
        return new PostAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final PostAdapter.ViewHolder holder, final int position) {
    holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<CardElement> items){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView usuario, fecha, texto;

        ViewHolder(View itemView){
            super(itemView);
            usuario = itemView.findViewById(R.id.usuarioname);
            fecha = itemView.findViewById(R.id.horapost);
            texto = itemView.findViewById(R.id.textPost);
        }

        void bindData(final CardElement item){
            usuario.setText(item.getUsuario());
            fecha.setText(item.getFecha());
            texto.setText(item.getTexto());

        }
    }
}
