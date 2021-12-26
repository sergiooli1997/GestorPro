package com.escom.gestorpro.models;

public class Buzon {
    private String id;
    private String link;
    private String idProyecto;

    public Buzon(){

    }

    public Buzon(String id, String link, String idProyecto) {
        this.id = id;
        this.link = link;
        this.idProyecto = idProyecto;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }
}
