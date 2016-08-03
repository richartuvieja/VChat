package com.kuikos.vchat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martin on 01/10/2015.
 */
public class Varios {

    public static boolean validateEmail(String email) {

        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static String sanearMail(String mail)
    {
        return mail.replace(".","").replace("#","").replace("$","").replace("[","").replace("]","");
    }


    public static String sanearUsuario(String usuario)
    {
        return usuario.replace(" ","").toLowerCase();
    }
}
