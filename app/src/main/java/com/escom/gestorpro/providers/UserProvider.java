package com.escom.gestorpro.providers;

import com.escom.gestorpro.models.Users;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {
    private CollectionReference mCollection;

    public UserProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Usuarios");
    }

    public Task<DocumentSnapshot> getUser(String id) {
        return mCollection.document(id).get();
    }

    public Task<Void> create(Users user) {
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(Users user) {
        Map<String, Object> map = new HashMap<>();
        map.put("usuario", user.getUsuario());
        map.put("celular", user.getCelular());
        return mCollection.document(user.getId()).update(map);
    }
}
