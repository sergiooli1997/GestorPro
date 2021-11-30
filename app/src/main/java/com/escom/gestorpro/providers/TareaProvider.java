package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Tarea;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Task<Void> update(Tarea tarea){
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", tarea.getNombre());
        map.put("descripcion", tarea.getDescripcion());
        map.put("prioridad", tarea.getPrioridad());
        map.put("repositorio", tarea.getRepositorio());
        map.put("fecha_inicio", tarea.getFecha_inicio());
        map.put("fecha_fin", tarea.getFecha_fin());
        return mCollection.document(tarea.getId()).update(map);

    }

    public Task<Void> delete(String id){return mCollection.document(id).delete();}

    public Task<Void> updateAvance(String id, int value){
        return mCollection.document(id).update("completado", value);
    }
    public Task<Void> updateRetraso(String id, int value){
        return mCollection.document(id).update("retraso", value);
    }

    public Query getTareasTotalByProyecto (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto);
    }

    public Query getTareaCompletoByProyecto (String idProyecto, int value){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("completado", value);
    }

    public Query getTareaCompletoByValorAllUser (List<String> proyectos, int value){
        return mCollection.whereIn("idProyecto", proyectos).whereEqualTo("completado", value);
    }
    public Query getTareaCompletoByValorByUser (String idUser, int value){
        return mCollection.whereEqualTo("idUsuario", idUser).whereEqualTo("completado", value);
    }

    public Query getTareaRetrasoAllUser (List<String> proyectos, int value){
        return mCollection.whereIn("idProyecto", proyectos).whereEqualTo("retraso", value);
    }
    public Query getTareaRetrasoByUser (String idUser, int value){
        return mCollection.whereEqualTo("idUsuario", idUser).whereEqualTo("retraso", value);
    }

    public Query getTareasConRetraso (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("retraso", 1);
    }

    public Query getTareasRepoVacio (String idProyecto){
        return mCollection.whereEqualTo("idProyecto", idProyecto).whereEqualTo("repositorio", "");
    }
}
