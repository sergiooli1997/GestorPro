package com.escom.gestorpro.models;

public class Users {
    private String id;
    private String email;
    private String usuario;
    private String celular;
    private String imageProfile;
    private String imageCover;


    public Users() {

    }

    public Users(String id, String email, String usuario, String celular, String imageProfile ,String imageCover) {
        this.id = id;
        this.email = email;
        this.usuario = usuario;
        this.celular = celular;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
