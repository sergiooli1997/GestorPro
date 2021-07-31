package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Comment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommentProvider {

    CollectionReference mCollection;

    public CommentProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Comentarios");
    }

    public Task<Void> create(Comment comment){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        comment.setId(id);
        return document.set(comment);
    }

    public Query getCommentsByPost(String idPost) {
        return mCollection.whereEqualTo("idPost", idPost);
    }

}
