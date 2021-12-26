package com.escom.gestorpro.models;

import java.util.List;

public class Proyecto {
    private String id;
    private String nombre;
    private long fecha_inicio;
    private long fecha_fin;
    private String codigo;
    private List<String> equipo;
    private String idCliente;
    private int completo;
    private int calificacion;
    private String cuestionario;

    public Proyecto(){

    }

    public Proyecto(String id, String nombre, long fecha_inicio, long fecha_fin, String codigo, List<String> equipo, String idCliente, int completo, int calificacion, String cuestionario) {
        this.id = id;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.codigo = codigo;
        this.equipo = equipo;
        this.idCliente = idCliente;
        this.completo = completo;
        this.calificacion = calificacion;
        this.cuestionario = cuestionario;
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

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getCompleto() {
        return completo;
    }

    public void setCompleto(int completo) {
        this.completo = completo;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(String cuestionario) {
        this.cuestionario = cuestionario;
    }
}
