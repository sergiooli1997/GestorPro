package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Post");
    }

    public Task<Void> save(Post post){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        post.setId(id);
        return document.set(post);
    }

    public Query getAll() {
        return mCollection.orderBy("fecha", Query.Direction.DESCENDING);
    }

    public Query getPostByProyectos(List<String> proyectos){
        return mCollection.whereIn("idProyecto", proyectos).orderBy("fecha", Query.Direction.DESCENDING);
    }

    public Query getPostByUser(String id) {
        return mCollection.whereEqualTo("usuario", id).orderBy("fecha", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getPostById(String id) {
        return mCollection.document(id).get();
    }

    public Query getPostCriticosByProyecto(String idProyecto, String value){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("tipo", value);
    }

    public Query getPostByProyecto(String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Task<Void> delete(String id){return mCollection.document(id).delete();}
}
