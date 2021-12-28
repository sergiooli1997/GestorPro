package com.escom.gestorpro.models;

public class Archivo {
    private String id;
    private String nombre;
    private String descripcion;
    private String idProyecto;
    private String link;

    public Archivo(){

    }

    public Archivo(String id, String nombre, String descripcion, String idProyecto, String link) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idProyecto = idProyecto;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
