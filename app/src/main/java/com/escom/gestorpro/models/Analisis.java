package com.escom.gestorpro.models;

public class Analisis {
    private double tareasRepositorio;
    private double tareasRetraso;
    private int mensajesCliente;
    private int postProyecto;

    public Analisis(){

    }

    public Analisis(double tareasRepositorio, double tareasRetraso, int mensajesCliente, int postProyecto) {
        this.tareasRepositorio = tareasRepositorio;
        this.tareasRetraso = tareasRetraso;
        this.mensajesCliente = mensajesCliente;
        this.postProyecto = postProyecto;
    }

    public double getTareasRepositorio() {
        return tareasRepositorio;
    }

    public void setTareasRepositorio(double tareasRepositorio) {
        this.tareasRepositorio = tareasRepositorio;
    }

    public double getTareasRetraso() {
        return tareasRetraso;
    }

    public void setTareasRetraso(double tareasRetraso) {
        this.tareasRetraso = tareasRetraso;
    }

    public int getMensajesCliente() {
        return mensajesCliente;
    }

    public void setMensajesCliente(int mensajesCliente) {
        this.mensajesCliente = mensajesCliente;
    }

    public int getPostProyecto() {
        return postProyecto;
    }

    public void setPostProyecto(int postProyecto) {
        this.postProyecto = postProyecto;
    }
}
