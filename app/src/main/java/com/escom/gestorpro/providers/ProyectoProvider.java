package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Proyecto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ProyectoProvider {

    CollectionReference mCollection;

    public ProyectoProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Proyectos");
    }

    public Query getProyectoByUser(String id) {
        return mCollection.whereArrayContains("equipo", id);
    }

    public Task<DocumentSnapshot> getProyectoById(String id) {
        return mCollection.document(id).get();
    }

    public Task<QuerySnapshot> getIdByCode(String codigo) {
        return mCollection.whereEqualTo("codigo", codigo).get();
    }

    public Query getCodigo(String codigo) {
        return mCollection.whereEqualTo("codigo", codigo);
    }

    public Task<Void> save(Proyecto proyecto){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        proyecto.setId(id);
        return document.set(proyecto);
    }

    public Task<Void> addUser(String idProyecto, String usuario){
        return mCollection.document(idProyecto).update("equipo", FieldValue.arrayUnion(usuario));
    }

    public Task<Void> delete(String id){return mCollection.document(id).delete();}

}
