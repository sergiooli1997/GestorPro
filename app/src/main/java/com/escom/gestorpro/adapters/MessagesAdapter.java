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
import com.escom.gestorpro.activities.ChatActivity;
import com.escom.gestorpro.models.Message;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.escom.gestorpro.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;

    public MessagesAdapter(FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String chatId = document.getId();
        holder.textViewMessage.setText(message.getMessage());

        String relativeTime = RelativeTime.getTimeAgo(message.getTimestamp(), context);
        holder.textViewDate.setText(relativeTime);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewDate;
        ImageView imageViewViewed;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewDate = view.findViewById(R.id.textViewDateMessage);
            imageViewViewed = view.findViewById(R.id.imageViewViewedMessage);
            viewHolder = view;
        }
    }

}
