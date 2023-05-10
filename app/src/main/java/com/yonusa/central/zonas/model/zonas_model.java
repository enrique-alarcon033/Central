package com.yonusa.central.zonas.model;


import java.util.ArrayList;

/**
 * Created by ExpoCode Tech http://expocodetech.com
 */

public class zonas_model {


    private String idNet;
    private String idNodo;
    private String idZona;
    private String nombre;
    private String ubicacion;
    private String estatus;
    private String alerta;
    private String bateria;

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public ArrayList<zonasm> getZonasms() {
        return zonasms;
    }

    public void setZonasms(ArrayList<zonasm> zonasms) {
        this.zonasms = zonasms;
    }

    private ArrayList<zonasm> zonasms;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getIdNet() {
        return idNet;
    }

    public void setIdNet(String idNet) {
        this.idNet = idNet;
    }

    public String getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(String idNodo) {
        this.idNodo = idNodo;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    private String estado;


}
