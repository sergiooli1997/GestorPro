package com.escom.gestorpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResponsablesAdapter extends RecyclerView.Adapter<ResponsablesAdapter.ViewHolder> {
    private List<ResponsablesCardElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ResponsablesAdapter(List<ResponsablesCardElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @NonNull
    @Override
    public ResponsablesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_responsables, null);
        return new ResponsablesAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ResponsablesAdapter.ViewHolder holder, final int position) {
    holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<ResponsablesCardElement> items){mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView responsable;

        ViewHolder(View itemView){
            super(itemView);
            responsable = itemView.findViewById(R.id.nombreResponsable);
        }

        void bindData(final ResponsablesCardElement item){
            responsable.setText(item.getResponsable());

        }
    }
}
