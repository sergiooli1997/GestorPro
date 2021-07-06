package com.escom.gestorpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {
    Context context;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        holder.textViewDesc.setText(post.getTexto());
        if (post.getImage() != null) {
            if (!post.getImage().isEmpty()) {
                Picasso.with(context).load(post.getImage()).into(holder.imageViewPost);
            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDesc;
        ImageView imageViewPost;

        public ViewHolder(View view){
            super(view);

            textViewDesc = view.findViewById(R.id.textPost);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);

        }
    }

}
