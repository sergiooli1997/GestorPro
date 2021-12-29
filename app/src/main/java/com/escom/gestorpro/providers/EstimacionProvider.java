package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Estimacion;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class EstimacionProvider {
    CollectionReference mCollection;

    public EstimacionProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Estimaciones");
    }

    public Task<Void> save(Estimacion estimacion){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        estimacion.setId(id);
        return document.set(estimacion);
    }

    public Query getEstimacionesByProyecto(String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Task<Void> deleteEstimacion(String id){return mCollection.document(id).delete();}
}
