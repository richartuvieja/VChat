package com.kuikos.vchat.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class LogInFragment extends Fragment {

    private FirebaseAuth mAuth;
    ChatApplication application;
    ProgressDialog barProgressDialog;

    public LogInFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= (ChatApplication)LogInFragment.this.getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        TextView logo = (TextView) view.findViewById(R.id.login_nombre);
        Typeface myTypeface = Typeface.createFromAsset(this.getActivity().getAssets(), "fuente.ttf");
        logo.setTypeface(myTypeface);

        final EditText txtv_user = (EditText) view.findViewById(R.id.login_name);
        final EditText txtv_pass = (EditText) view.findViewById(R.id.login_password);
        TextView btn_ok = (TextView) view.findViewById(R.id.login_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_mail = txtv_user.getText().toString().trim();
                String edit_pass = txtv_pass.getText().toString().trim();

                if (validar_campos(edit_pass,edit_mail))
                    login(edit_mail, edit_pass);
            }
        });



        return view;
    }


    private void login(final String email, String password) {
        barProgressDialog = new ProgressDialog(LogInFragment.this.getActivity());
        barProgressDialog.setMessage("Logueando...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            barProgressDialog.hide();
                            Toast.makeText(LogInFragment.this.getActivity(), "Error en el login",Toast.LENGTH_SHORT).show();
                        }else{
                            levantarUsuario(email);
                        }
                    }
                });

    }

    private void levantarUsuario(String email) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usuarios");

            myRef.child(Varios.sanearMail(email)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);
                            application.setUser(usuario);
                            application.setUsuarioLogueado(true);
                            Intent intent = new Intent(LogInFragment.this.getActivity(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("MSJ", "Error: " + databaseError.getMessage());
                            barProgressDialog.hide();
                        }
                    });
    }

    public boolean validar_campos(String usr_password,String usr_email) {

        Log.e("MSJ", "Se entró en la valicadcion");
        boolean pass_ok = true;
        boolean mail_ok = true;

        //Validación de email
        if (usr_email.isEmpty() && mail_ok) {
            Toast.makeText(this.getActivity(),"El mail no puede estar vacío.",Toast.LENGTH_LONG).show();
            mail_ok = false;
        }

        if (!Varios.validateEmail(usr_email) && mail_ok) {
            Toast.makeText(this.getActivity(),"Formato de mail invalido",Toast.LENGTH_LONG).show();
            mail_ok = false;
        }

        //Validación de contraseña
        if (usr_password.isEmpty() && pass_ok) {
            Toast.makeText(this.getActivity(),"La contraseña no puede estar vacía.",Toast.LENGTH_LONG).show();
            pass_ok = false;
        }

        return pass_ok && mail_ok;
    }
}
