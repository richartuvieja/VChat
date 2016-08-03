package com.kuikos.vchat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kuikos.vchat.model.Usuario;
import com.kuikos.vchat.presistence.Preferences;

/**
 * @author Jenny Tong (mimming)
 * @since 12/5/14
 * <p/>
 * Initialize Firebase with the application context. This must happen before the client is used.
 */
public class ChatApplication extends android.app.Application {

    Usuario usuario;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Preferences.quieroNotificacionesEspecialesActivadas(this) && !Preferences.isNotificacionesEspecialesActivadas(this)) {
            FirebaseMessaging.getInstance().subscribeToTopic("global");
            Preferences.setNotificacionesEspecialesActivadas(this, true);
        }

        if (Preferences.quieroNotificacionesMsjActivadas(this) && !Preferences.isNotificacionesMsjActivadas(this)) {
            FirebaseMessaging.getInstance().subscribeToTopic("newMessage");
            Preferences.setNotificacionesMsjActivadas(this, true);
        }
    }

    public void setUsuarioLogueado(boolean bool) {
        Preferences.setUsuarioLogueado(this, bool);
    }

    public boolean isUsuarioLogueado() {
        return Preferences.isUsuarioLogueado(this);
    }

    public void setUser(Usuario usuario) {
        Preferences.setUsuario(this, usuario);
        this.usuario = usuario;
    }

    public Usuario getUser() {
        if (usuario == null)
            return Preferences.getUsuario(this);
        else
            return usuario;
    }
}
