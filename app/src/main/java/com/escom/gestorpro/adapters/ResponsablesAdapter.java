package com.escom.gestorpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.models.Users;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResponsablesAdapter extends FirestoreRecyclerAdapter<Users, ResponsablesAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;

    public ResponsablesAdapter(FirestoreRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Users user) {
        if (user.getImageProfile() != null){
            if (!user.getImageProfile().isEmpty()){
                Picasso.with(context).load(user.getImageProfile()).into(holder.circleImageViewResponsable);
            }

        }
        getUserInfo(user.getId(), holder);
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
                    //If del rol
                    holder.textViewRol.setText("Rol de usuario");
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_responsables, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewUsername;
        TextView textViewRol;
        CircleImageView circleImageViewResponsable;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);

            textViewUsername = view.findViewById(R.id.textViewUsernameResponsable);
            textViewRol = view.findViewById(R.id.textViewRolResponsable);
            circleImageViewResponsable = view.findViewById(R.id.circleImageProfileResponsable);
            viewHolder = view;
        }
    }
}
