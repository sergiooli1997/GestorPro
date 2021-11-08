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

import java.util.HashMap;
import java.util.Map;

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

    public Query getProyectoByCliente(String id) {
        return mCollection.whereEqualTo("idCliente", id);
    }

    public Task<QuerySnapshot> getIdByCode(String codigo) {
        return mCollection.whereEqualTo("codigo", codigo).get();
    }

    public Query getCodigo(String codigo) {
        return mCollection.whereEqualTo("codigo", codigo);
    }

    public Task<QuerySnapshot> findLider(String idProyecto){
        return mCollection.whereEqualTo("id", idProyecto).get();
    }

    public Query getProyectoCompletoByUser(String id, int value) {
        return mCollection.whereArrayContains("equipo", id).whereEqualTo("completo", value);
    }

    public Query getProyectoCompletoByCliente(String id, int value) {
        return mCollection.whereEqualTo("idCliente", id).whereEqualTo("completo", value);
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

    public Task<Void> updateCliente(String id, String idCliente){
        return mCollection.document(id).update("idCliente", idCliente);
    }

    public Task<Void> update(Proyecto proyecto) {
        Map<String, Object> map = new HashMap<>();
        map.put("fecha_fin", proyecto.getFecha_fin());
        map.put("fecha_inicio", proyecto.getFecha_inicio());
        return mCollection.document(proyecto.getId()).update(map);
    }

    public Task<Void> updateAvance(String id, int value){
        return mCollection.document(id).update("completo", value);
    }

    public Task<Void> deleteProyecto(String id){return mCollection.document(id).delete();}

    public Task<Void> deleteUsuarioFromProyecto(String idProyecto , String idUsuario){
        return mCollection.document(idProyecto).update("equipo", FieldValue.arrayRemove("equipo", idUsuario));
    }

}
