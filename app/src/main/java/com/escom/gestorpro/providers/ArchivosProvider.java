package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Archivo;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ArchivosProvider {
    CollectionReference mCollection;

    public ArchivosProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Archivos");
    }

    public Task<Void> save(Archivo archivo){
        DocumentReference document = mCollection.document();
        String id = document.getId();
        archivo.setId(id);
        return document.set(archivo);
    }

    public Query getArchivosByProyecto(String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Task<Void> deleteArchivo(String id){return mCollection.document(id).delete();}
}
