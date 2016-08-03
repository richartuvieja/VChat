package com.kuikos.vchat.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kuikos.vchat.ChatApplication;
import com.firebase.androidchat.R;
import com.kuikos.vchat.Varios;
import com.kuikos.vchat.activities.MainActivity;
import com.kuikos.vchat.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;

    ProgressDialog barProgressDialog;

    EditText edit_user;
    EditText edit_mail;
    EditText edit_pass;
    RadioGroup radio_gender;

    TextInputLayout input_user;
    TextInputLayout input_mail;
    TextInputLayout input_pass;


    ChatApplication application;


    public SignInFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= (ChatApplication) SignInFragment.this.getActivity().getApplication();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        try {
            barProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        TextView logo = (TextView) view.findViewById(R.id.signin_nombre);
        Typeface myTypeface = Typeface.createFromAsset(this.getActivity().getAssets(), "fuente.ttf");
        logo.setTypeface(myTypeface);

        edit_user = (EditText) view.findViewById(R.id.signin_name);
        edit_mail = (EditText) view.findViewById(R.id.signin_email);
        edit_pass = (EditText) view.findViewById(R.id.signin_pass);
        radio_gender = (RadioGroup) view.findViewById(R.id.signin_gender);
        input_user = (TextInputLayout) view.findViewById(R.id.signin_input_name);
        input_mail = (TextInputLayout) view.findViewById(R.id.signin_input_email);
        input_pass = (TextInputLayout) view.findViewById(R.id.signin_input_pass);

        Button ok = (Button) view.findViewById(R.id.signin_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usr_username = edit_user.getText().toString().trim();
                String usr_email = edit_mail.getText().toString().trim();
                String usr_password = edit_pass.getText().toString().trim();
                String usr_gender = "N";

                int genero = radio_gender.getCheckedRadioButtonId();
                switch (genero){
                    case R.id.radio_hembra: usr_gender="F";
                        break;
                    case R.id.radio_macho: usr_gender="M";
                        break;
                }


                //Validar que el celular no tenga una cuenta creada con ese id


                if (validar_campos(usr_username, usr_email, usr_password))
                    verificarDispositivo(usr_username, usr_email, usr_password,usr_gender);

            }
        });

        return view;
    }

    private void verificarDispositivo(final String reg_username, final String reg_email, final String reg_pass, final String reg_gender) {

        barProgressDialog = new ProgressDialog(SignInFragment.this.getActivity());
        barProgressDialog.setMessage("Validando usuario");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("dispositivos");

        String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        myRef.child(android_id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barProgressDialog.hide();
                            Toast.makeText(SignInFragment.this.getActivity(), "No podes crear mas de una cuenta", Toast.LENGTH_LONG).show();
                        }
                        else
                            verificarUsuario(reg_username, reg_email, reg_pass,reg_gender);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MSJ", "Error: " + databaseError.getMessage());
                        barProgressDialog.hide();
                        Toast.makeText(SignInFragment.this.getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void verificarUsuario(final String reg_username, final String reg_email, final String reg_pass, final String reg_gender) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usernames");

        myRef.child(Varios.sanearUsuario(reg_username)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barProgressDialog.hide();
                            Toast.makeText(SignInFragment.this.getActivity(), "Ya existe un usuario con ese nombre", Toast.LENGTH_LONG).show();
                        }
                        else
                            register(reg_username, reg_email, reg_pass,reg_gender);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MSJ", "Error: " + databaseError.getMessage());
                        barProgressDialog.hide();
                        Toast.makeText(SignInFragment.this.getActivity(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void register(final String reg_username, final String reg_email, final String reg_pass, final String reg_gender) {

        barProgressDialog.setMessage("Iniciando registro");

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(reg_email, reg_pass)
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("MSJ", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            Usuario new_usr = new Usuario(android_id,reg_username,reg_email,reg_gender,"-");
                            createUser(new_usr);

                        } else {
                            barProgressDialog.hide();
                            if (task.getException()!=null)
                                Toast.makeText(SignInFragment.this.getActivity(), "Error al iniciar registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(SignInFragment.this.getActivity(), "Error al iniciar registro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    private void createUser(final Usuario usr){

        barProgressDialog.setMessage("Creando usuario");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");

        //Marca el dispositivo
        database.getReference().child("dispositivos").child(usr.device_id).setValue(usr.reg_email);
        database.getReference().child("usernames").child(Varios.sanearUsuario(usr.reg_username)).setValue(usr.reg_email);

        myRef.child(Varios.sanearMail(usr.reg_email)).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                barProgressDialog.dismiss();

                if (!task.isSuccessful()) {
                    Toast.makeText(SignInFragment.this.getActivity(), "Error al crear usuario.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(SignInFragment.this.getActivity(), "Bienvenido :)", Toast.LENGTH_LONG).show();
                    application.setUser(usr);
                    application.setUsuarioLogueado(true);
                    Intent intent = new Intent(SignInFragment.this.getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public boolean validar_campos(String usr_username, String usr_email, String usr_password) {

        Log.e("MSJ", "Se entró en la valicadcion");
        boolean user_ok = true;
        boolean pass_ok = true;
        boolean mail_ok = true;
        boolean genero_ok = true;

        //Validación de usuario
        if (usr_username.isEmpty()) {
            input_user.setError("Nombre de usuario vacío.");
            user_ok = false;
        }

        if (usr_username.length() < 4 && user_ok) {
            input_user.setError("Nombre de usuario menor a 4 caracteres.");
            user_ok = false;
        }

        //Validación de email
        if (usr_email.isEmpty() && mail_ok) {
            input_mail.setError("Email vacío.");
            mail_ok = false;
        }

        if (!Varios.validateEmail(usr_email) && mail_ok) {
            input_mail.setError("Formato incorrecto.");
            mail_ok = false;
        }

        //Validación de contraseña
        if (usr_password.isEmpty() && pass_ok) {
            input_pass.setError("Password vacío.");
            pass_ok = false;
        }

        if (usr_password.length()<6 && pass_ok) {
            input_pass.setError("Contraseña menor a 6 caracteres");
            pass_ok = false;
        }



        //Resetea si esta ok
        if (user_ok)
            input_user.setErrorEnabled(false);

        if (mail_ok)
            input_mail.setErrorEnabled(false);

        if (pass_ok)
            input_pass.setErrorEnabled(false);


        //checkea el genero
        if (radio_gender.getCheckedRadioButtonId() == -1)
        {
            genero_ok = false;
            Toast.makeText(SignInFragment.this.getActivity(),"Seleccione un sexo",Toast.LENGTH_LONG).show();
        }



        return user_ok && pass_ok && mail_ok && genero_ok;
    }


}
