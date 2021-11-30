package com.escom.gestorpro.models;

public class Tarea {
    private String id;
    private String idProyecto;
    private String idUsuario;
    private String nombre;
    private String descripcion;
    private String repositorio;
    private long fecha_inicio;
    private long fecha_fin;
    private int completado;
    private int retraso;
    private String prioridad;

    public Tarea(){

    }

    public Tarea(String id, String idProyecto, String idUsuario, String nombre, String descripcion, String repositorio, long fecha_inicio, long fecha_fin, int completado, int retraso, String prioridad) {
        this.id = id;
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.repositorio = repositorio;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.completado = completado;
        this.retraso = retraso;
        this.prioridad = prioridad;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(String repositorio) {
        this.repositorio = repositorio;
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

    public int getCompletado() {
        return completado;
    }

    public void setCompletado(int completado) {
        this.completado = completado;
    }

    public int getRetraso() {
        return retraso;
    }

    public void setRetraso(int retraso) {
        this.retraso = retraso;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
