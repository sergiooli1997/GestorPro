package com.escom.gestorpro.models;

public class Riesgo {
    private String id;
    private String nombre;
    private String probabilidad;
    private String impacto;
    private String clasificacion;
    private String idProyecto;
    private String link;

    public Riesgo(){

    }

    public Riesgo(String id, String nombre, String probabilidad, String impacto, String clasificacion, String idProyecto, String link) {
        this.id = id;
        this.nombre = nombre;
        this.probabilidad = probabilidad;
        this.impacto = impacto;
        this.clasificacion = clasificacion;
        this.idProyecto = idProyecto;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(String probabilidad) {
        this.probabilidad = probabilidad;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
