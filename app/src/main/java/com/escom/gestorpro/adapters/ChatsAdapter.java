package com.escom.gestorpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.escom.gestorpro.R;
import com.escom.gestorpro.activities.ChatActivity;
import com.escom.gestorpro.models.Chat;
import com.escom.gestorpro.providers.AuthProvider;
import com.escom.gestorpro.providers.ChatsProvider;
import com.escom.gestorpro.providers.MessagesProvider;
import com.escom.gestorpro.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {
    Context context;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    MessagesProvider mMessagesProvider;

    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context) {
        super(options);
        this.context = context;
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mChatsProvider = new ChatsProvider();
        mMessagesProvider = new MessagesProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String chatId = document.getId();
        if(mAuthProvider.getUid().equals(chat.getIdUser1())){
            getUserInfo(chat.getIdUser2(), holder);
        }
        else{
            getUserInfo(chat.getIdUser1(), holder);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity(chatId, chat.getIdUser1(), chat.getIdUser2());
            }
        });

        getLastMessage(chatId, holder.textViewLastMessage);
        String idSender = "";
        if(mAuthProvider.getUid().equals(chat.getIdUser1())){
            idSender = chat.getIdUser2();
        }
        else{
            idSender = chat.getIdUser1();
        }

        getMessageNotRead(chatId, idSender, holder.textViewMessageNotRead, holder.frameLayoutMessageNotRead);
    }

    private void getMessageNotRead(String chatId, String idSender, TextView textViewMessageNotRead, FrameLayout frameLayoutMessageNotRead) {
        mMessagesProvider.getMessageByChatAndSender(chatId, idSender).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                if(error == null){
                    int size = queryDocumentSnapshots.size();
                    if(size > 0){
                        frameLayoutMessageNotRead.setVisibility(View.VISIBLE);
                        textViewMessageNotRead.setText(String.valueOf(size));
                    }
                    else{
                        frameLayoutMessageNotRead.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void getLastMessage(String chatId, TextView textViewLastMessage) {
        mMessagesProvider.getLastMessage(chatId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                if(size > 0){
                    String lastMessage = queryDocumentSnapshots.getDocuments().get(0).getString("message");
                    textViewLastMessage.setText(lastMessage);
                }
            }
        });
    }

    private void goToChatActivity(String chatId, String idUser1, String idUser2) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idChat", chatId);
        intent.putExtra("idUser1", idUser1);
        intent.putExtra("idUser2", idUser2);
        context.startActivity(intent);
    }

    private void getUserInfo(String idUser, final ViewHolder holder){
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("usuario")){
                        String username = documentSnapshot.getString("usuario");
                        holder.textViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if(!imageProfile.isEmpty()){
                                Picasso.with(context).load(imageProfile).into(holder.circleImageChat);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat, parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewLastMessage;
        TextView textViewMessageNotRead;
        CircleImageView circleImageChat;
        FrameLayout frameLayoutMessageNotRead;
        View viewHolder;

        public ViewHolder(View view){
            super(view);

            textViewUsername = view.findViewById(R.id.textViewUsernameChat);
            textViewLastMessage = view.findViewById(R.id.textViewLastMessage);
            circleImageChat = view.findViewById(R.id.circleImageChat);
            textViewMessageNotRead = view.findViewById(R.id.textViewMessageNotRead);
            frameLayoutMessageNotRead = view.findViewById(R.id.frameLayoutMessageNotRead);
            viewHolder = view;
        }
    }

}
