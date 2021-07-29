package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Proyecto;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProyectoProvider {

    CollectionReference mCollection;

    public ProyectoProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Proyectos");
    }

    public Task<Void> save(Proyecto proyecto){
        return mCollection.document().set(proyecto);
    }

    public Task<Void> delete(String id){return mCollection.document(id).delete();}

}
