package com.dinomobile.olxapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.activity.SplashActivity;
import com.dinomobile.olxapp.config.ConfigFirebase;
import com.dinomobile.olxapp.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        iniComponents(v);
        configInterface();

        return v;
    }

    public void configInterface() {

        btnLogin.setOnClickListener(v -> validate());

    }

    public void validate() {

        String email = editEmail.getText().toString();
        String passord = editPassword.getText().toString();

        if (!email.isEmpty()) {
            if (!passord.isEmpty()) {

                User u = new User();
                u.setEmail(email);
                u.setPassword(passord);

                loginUser(u);

            } else {
                snackBar("Ops! Digite sua senha.");
            }
        } else {
            snackBar("Ops! Digite seu e-mail.");
        }

    }

    public void loginUser(User u) {

        btnLogin.setText("CARREGANDO");
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(
                u.getEmail(),
                u.getPassword()).addOnCompleteListener(getActivity(),
                task -> {

                    if (task.isSuccessful()) {

                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        startActivity(i);

                    } else {

                        progressBar.setVisibility(View.GONE);
                        btnLogin.setText("CONTINUAR COM EMAIL E SENHA");
                        String message;

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            message = "Ops! Você ainda não está cadastrado.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            message = "Ops! Endereço de e-mail ou senha estão incorretos.";
                        } catch (Exception e) {
                            message = "Ops! Algo saiu errado. Tente Novamente!";
                            e.printStackTrace();
                        }
                        snackBar(message);

                    }

                });

    }

    public void snackBar(String message) {

        Snackbar.make(getView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.white))
                .setTextColor(getResources().getColor(R.color.colorText))
                .show();
    }

    public void iniComponents(View v) {

        editEmail = v.findViewById(R.id.editEmailLogin);
        editPassword = v.findViewById(R.id.editSenhaLogin);
        btnLogin = v.findViewById(R.id.btnEntrarLogin);
        progressBar = v.findViewById(R.id.progressBarLogin);

        auth = ConfigFirebase.getAuth();
    }

}