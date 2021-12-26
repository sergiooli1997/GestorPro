package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Buzon;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BuzonProvider {
    CollectionReference mCollection;

    public BuzonProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Buzones");
    }

    public Task<Void> save(Buzon buzon){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        buzon.setId(id);
        return document.set(buzon);
    }

    public Query getBuzonByProyecto(String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Task<Void> deleteBuzon(String id){return mCollection.document(id).delete();}

}
