package com.kuikos.vchat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.firebase.androidchat.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kuikos.vchat.presistence.Preferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {


    public ConfigFragment() {
        // Required empty public constructor
    }

    Switch switch_generales;
    Switch switch_mensajes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =inflater.inflate(R.layout.fragment_config, container, false);

        switch_generales = (Switch) v.findViewById(R.id.config_switch_generales);
        switch_mensajes = (Switch) v.findViewById(R.id.config_switch_mensajes);

        switch_generales.setChecked(Preferences.isNotificacionesEspecialesActivadas(this.getActivity()));
        switch_mensajes.setChecked(Preferences.isNotificacionesMsjActivadas(this.getActivity()));

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
            if (switch_generales.isChecked()) FirebaseMessaging.getInstance().subscribeToTopic("global");
            else FirebaseMessaging.getInstance().unsubscribeFromTopic("global");
            Preferences.setNotificacionesEspecialesActivadas(this.getActivity(), switch_generales.isChecked());
            Preferences.setQuieroNotificacionesEspecialesActivadas(this.getActivity(), switch_generales.isChecked());

            if (switch_mensajes.isChecked()) FirebaseMessaging.getInstance().subscribeToTopic("newMessage");
            else FirebaseMessaging.getInstance().unsubscribeFromTopic("newMessage");
            Preferences.setNotificacionesMsjActivadas(this.getActivity(), switch_mensajes.isChecked());
            Preferences.setQuieroNotificacionesMsjActivadas(this.getActivity(), switch_mensajes.isChecked());
    }

}
