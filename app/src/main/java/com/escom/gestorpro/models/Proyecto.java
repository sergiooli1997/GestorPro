package com.escom.gestorpro.models;

import java.util.List;

public class Proyecto {
    private String id;
    private String nombre;
    private long fecha_inicio;
    private long fecha_fin;
    private String codigo;
    List<String> equipo;

    public Proyecto(){

    }

    public Proyecto(String id, String nombre, long fecha_inicio, long fecha_fin, String codigo, List<String> equipo) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.codigo = codigo;
        this.equipo = equipo;
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

    public long getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(long fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public long getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(long fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<String> getEquipo() {
        return equipo;
    }

    public void setEquipo(List<String> equipo) {
        this.equipo = equipo;
    }
}
