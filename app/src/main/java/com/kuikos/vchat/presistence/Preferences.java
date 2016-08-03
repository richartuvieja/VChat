package com.kuikos.vchat.presistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.kuikos.vchat.model.Usuario;

/**
 * Created by uhald on 3/7/2016.
 */
 public class Preferences {

    static final String prefs_name = "PREFS";

    static final String is_usr_logueado = "is_usr_logueado";

    static final String usr_device_id = "usr_device_id";
    static final String usr_usrname = "usr_usrname";
    static final String usr_email = "usr_email";
    static final String usr_pais = "usr_pais";
    static final String usr_genero = "usr_genero";

    static final String notif_especiales_quiero_activadas = "notif_especiales_quiero_activadas";
    static final String notif_especiales_estan_activadas = "notif_especiales_estan_activadas";

    static final String notif_msj_quiero_activadas = "notif_msj_quiero_activadas";
    static final String notif_msj_estan_activadas = "notif_msj_estan_activadas";


    //Notificaciones especiales
    static public boolean isNotificacionesEspecialesActivadas(Context context){
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE).getBoolean(notif_especiales_estan_activadas,false);
    }
    static public void setNotificacionesEspecialesActivadas(Context context, boolean bool){
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notif_especiales_estan_activadas,bool);
        editor.apply();
    }

    static public boolean quieroNotificacionesEspecialesActivadas(Context context){
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE).getBoolean(notif_especiales_quiero_activadas,true);
    }
    static public void setQuieroNotificacionesEspecialesActivadas(Context context, boolean bool){
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notif_especiales_quiero_activadas,bool);
        editor.apply();
    }

    //Notificaciones especiales
    static public boolean isNotificacionesMsjActivadas(Context context){
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE).getBoolean(notif_msj_estan_activadas,false);
    }
    static public void setNotificacionesMsjActivadas(Context context, boolean bool){
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notif_msj_estan_activadas,bool);
        editor.apply();
    }

    static public boolean quieroNotificacionesMsjActivadas(Context context){
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE).getBoolean(notif_msj_quiero_activadas,true);
    }
    static public void setQuieroNotificacionesMsjActivadas(Context context, boolean bool){
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notif_msj_quiero_activadas,bool);
        editor.apply();
    }

    //Usuario

    static public boolean isUsuarioLogueado(Context context){
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE).getBoolean(is_usr_logueado,false);
    }

    static public void setUsuarioLogueado(Context context, boolean bool){
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(is_usr_logueado,bool);
        editor.apply();
    }

    static public void setUsuario(Context context, Usuario usuarui) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(usr_device_id, usuarui.device_id);
        editor.putString(usr_usrname, usuarui.reg_username);
        editor.putString(usr_email, usuarui.reg_email);
        editor.putString(usr_pais, usuarui.pais);
        editor.putString(usr_genero, usuarui.genero);
        editor.apply();
    }

    static public Usuario getUsuario(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);

        return new Usuario(
                sharedPref.getString(usr_device_id, "-"),
                sharedPref.getString(usr_usrname, "-"),
                sharedPref.getString(usr_email, "-"),
                sharedPref.getString(usr_genero, "-"),
                sharedPref.getString(usr_pais, "-")
        );
    }
}
