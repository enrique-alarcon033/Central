package com.yonusa.central.nodos.model;


import com.yonusa.central.zonas.model.zonasm;

import java.util.ArrayList;

/**
 * Created by ExpoCode Tech http://expocodetech.com
 */

public class nodo_model {



    private String idNodo;
    private String statusNodo;
    private String alertas;
    private String bateria;

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public String getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(String idNodo) {
        this.idNodo = idNodo;
    }

    public String getStatusNodo() {
        return statusNodo;
    }

    public void setStatusNodo(String statusNodo) {
        this.statusNodo = statusNodo;
    }

    public String getAlertas() {
        return alertas;
    }

    public void setAlertas(String alertas) {
        this.alertas = alertas;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    private String mensaje;




}
