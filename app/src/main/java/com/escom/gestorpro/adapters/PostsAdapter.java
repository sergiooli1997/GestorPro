package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.PostDetailActivity;
import com.escom.gestorpro.models.Like;
import com.escom.gestorpro.models.Post;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.LikesProvider;
import com.escom.gestorpro.providers.PostProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {
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

        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });

        getUserInfo(post.getUsuario(), holder);
        getNumberLikesByPost(postId, holder);
        checkIfExistLike(postId, mAuthProvider.getUid(), holder);
    }

    private void getNumberLikesByPost(String idPost, final ViewHolder holder){
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                int numberLikes = queryDocumentSnapshots.size();
                holder.textViewLike.setText(String.valueOf(numberLikes) + " Me gustas");
            }
        });
    }

    private void like(final Like like, final ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_grey);
                    mLikesProvider.delete(idLike);
                }
                else{
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_blue);
                    mLikesProvider.create(like);
                }
            }
        });
    }
    private void checkIfExistLike(String idPost, String idUser, final ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(idPost, idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_blue);
                }
                else{
                    holder.imageViewLike.setImageResource(R.drawable.icon_like_grey);
                }
            }
        });
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
        TextView textViewLike;
        ImageView imageViewPost;
        ImageView imageViewLike;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewDesc = view.findViewById(R.id.textPost);
            textViewLike = view.findViewById(R.id.textViewLikes);
            textViewUsername = view.findViewById(R.id.textViewUsernamePostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);

            viewHolder = view;
        }
    }

}
