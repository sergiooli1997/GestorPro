package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Riesgo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RiesgosProvider {
    CollectionReference mCollection;

    public RiesgosProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Riesgos");
    }

    public Task<Void> save(Riesgo riesgo){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        riesgo.setId(id);
        return document.set(riesgo);
    }

    public Task<Void> delete(String id){return mCollection.document(id).delete();}

    public Query getRiesgoByProyecto (String idProyecto, String clasificacion){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("clasificacion", clasificacion).orderBy("nombre", Query.Direction.ASCENDING);
    }
}
