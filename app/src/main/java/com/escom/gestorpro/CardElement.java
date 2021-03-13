package com.escom.gestorpro;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class CardElement {
    public String usuario;
    public String fecha;
    public String texto;

    public CardElement(String usuario, String fecha, String texto) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.texto = texto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
