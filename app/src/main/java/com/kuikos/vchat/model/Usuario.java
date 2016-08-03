package com.kuikos.vchat.model;

/**
 * Created by uhald on 2/7/2016.
 */
public class Usuario {
    public String device_id;
    public String reg_username;
    public String reg_email;
    public String pais;
    public String genero;

    public Usuario(){

    }

    public Usuario(String device_id, String reg_username, String reg_email, String genero, String pais) {
        this.device_id = device_id;
        this.reg_username = reg_username;
        this.reg_email = reg_email;
        this.pais = pais;
        this.genero = genero;
    }
}
