package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Tarea;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TareaProvider {
    CollectionReference mCollection;

    public TareaProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Tareas");
    }

    public Task<DocumentSnapshot> getTareaById(String id) {
        return mCollection.document(id).get();
    }

    public Query getAllTarea() {
        return mCollection.orderBy("id");
    }

    public Query getTareasByUser(String idUsuario) {
        return mCollection.whereEqualTo("idUsuario", idUsuario);
    }

    public Task<Void> save(Tarea tarea){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        tarea.setId(id);
        return document.set(tarea);
    }

    public Task<Void> updateAvance(String id, int value){
        return mCollection.document(id).update("completado", value);
    }
    public Task<Void> updateRetraso(String id, int value){
        return mCollection.document(id).update("retraso", value);
    }

    public Query getTareasTotalByProyecto (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Query getTareasCompletadas (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("completado", 1);
    }

    public Query getTareasConRetraso (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("retraso", 1);
    }
}
