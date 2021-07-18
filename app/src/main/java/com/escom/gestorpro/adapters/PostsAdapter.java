package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.PostDetailActivity;
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();

        holder.textViewDesc.setText(post.getTexto());
        if (post.getImage() != null) {
            if (!post.getImage().isEmpty()) {
                Picasso.with(context).load(post.getImage()).into(holder.imageViewPost);
            }
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

        getUserInfo(post.getUsuario(), holder);
    }

    private void getUserInfo(String usuario, final ViewHolder holder) {
        mUserProvider.getUser(usuario).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("usuario")){
                        String username = documentSnapshot.getString("usuario");
                        holder.textViewUsername.setText(username);
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDesc;
        TextView textViewUsername;
        ImageView imageViewPost;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewDesc = view.findViewById(R.id.textPost);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            textViewUsername = view.findViewById(R.id.textViewUsernamePostCard);
            viewHolder = view;
        }
    }

}
