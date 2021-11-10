package com.escom.gestorpro.providers;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FileProvider {
    StorageReference mStorage;

    public FileProvider(){
        mStorage = FirebaseStorage.getInstance().getReference().child("datos.csv");
    }

    public StorageReference getStorage() {
        return mStorage;
    }
}
