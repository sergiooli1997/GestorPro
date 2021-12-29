package com.escom.gestorpro.models;

public class Estimacion {
    private String id;
    private String nombre;
    private String idProyecto;
    private double PM;
    private double SF;
    private double PREC;
    private double FLEX;
    private double RESL;
    private double TEAM;
    private double PMAT;
    private double E;
    private double F;
    private double TDEV;
    private double size;
    private double salario;
    private double costo;
    private double otrosGastos;

    public Estimacion(){

    }

    public Estimacion(String id, String nombre, String idProyecto, double PM, double SF, double PREC, double FLEX, double RESL, double TEAM, double PMAT, double e, double f, double TDEV, double size, double salario, double costo, double otrosGastos) {
        this.id = id;
        this.nombre = nombre;
        this.idProyecto = idProyecto;
        this.PM = PM;
        this.SF = SF;
        this.PREC = PREC;
        this.FLEX = FLEX;
        this.RESL = RESL;
        this.TEAM = TEAM;
        this.PMAT = PMAT;
        E = e;
        F = f;
        this.TDEV = TDEV;
        this.size = size;
        this.salario = salario;
        this.costo = costo;
        this.otrosGastos = otrosGastos;
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

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public double getPM() {
        return PM;
    }

    public void setPM(double PM) {
        this.PM = PM;
    }

    public double getSF() {
        return SF;
    }

    public void setSF(double SF) {
        this.SF = SF;
    }

    public double getPREC() {
        return PREC;
    }

    public void setPREC(double PREC) {
        this.PREC = PREC;
    }

    public double getFLEX() {
        return FLEX;
    }

    public void setFLEX(double FLEX) {
        this.FLEX = FLEX;
    }

    public double getRESL() {
        return RESL;
    }

    public void setRESL(double RESL) {
        this.RESL = RESL;
    }

    public double getTEAM() {
        return TEAM;
    }

    public void setTEAM(double TEAM) {
        this.TEAM = TEAM;
    }

    public double getPMAT() {
        return PMAT;
    }

    public void setPMAT(double PMAT) {
        this.PMAT = PMAT;
    }

    public double getE() {
        return E;
    }

    public void setE(double e) {
        E = e;
    }

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public double getTDEV() {
        return TDEV;
    }

    public void setTDEV(double TDEV) {
        this.TDEV = TDEV;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getOtrosGastos() {
        return otrosGastos;
    }

    public void setOtrosGastos(double otrosGastos) {
        this.otrosGastos = otrosGastos;
    }
}
