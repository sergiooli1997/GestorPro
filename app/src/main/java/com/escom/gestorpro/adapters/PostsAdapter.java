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
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {
    Context context;
    UserProvider userProvider;
    AuthProvider mAuthProvider;

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

        userProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("usuario")) {
                        String username = documentSnapshot.getString("usuario");
                        holder.textViewUsuario.setText(username);
                    }

                    if (documentSnapshot.contains("imageProfile")) {
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null) {
                            if (!imageProfile.isEmpty()) {
                                Picasso.with(context).load(imageProfile).into(holder.mCircleImageProfile);
                            }
                        }
                    }
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts, parent,false);
        userProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDesc;
        TextView textViewUsuario;
        ImageView imageViewPost;
        CircleImageView mCircleImageProfile;

        public ViewHolder(View view){
            super(view);

            textViewDesc = view.findViewById(R.id.textPost);
            textViewUsuario = view.findViewById(R.id.usuarioname);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            mCircleImageProfile = view.findViewById(R.id.circleImageProfilePost);

        }
    }

}
