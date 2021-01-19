package com.dinomobile.olxapp.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import com.dinomobile.olxapp.activity.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFirebase {

    public static String getUserId() {
        FirebaseAuth auth = ConfigFirebase.getAuth();
        return auth.getCurrentUser().getUid();
    }

    public static FirebaseUser getUserFire() {
        FirebaseUser user = ConfigFirebase.getAuth().getCurrentUser();
        return user;
    }

    public static void statusAuth(Activity activity) {

        FirebaseAuth auth = ConfigFirebase.getAuth();

        if (auth.getCurrentUser() == null) {

            AlertDialog.Builder ad = new AlertDialog.Builder(activity);
            ad.setTitle("Entre ou Cadastre-se")
                    .setMessage("Para acessar esta sessão é necessário está logado na sua conta.")
                    .setCancelable(false).setPositiveButton("ENTRAR",
                    (dialog, which) -> {

                        Intent i = new Intent(activity, AuthActivity.class);
                        activity.startActivity(i);

                    }).create().show();

        }

    }

}
