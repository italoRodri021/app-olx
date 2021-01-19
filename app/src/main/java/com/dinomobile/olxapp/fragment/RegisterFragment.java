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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterFragment extends Fragment {

    private EditText editName, editEmail, editPassword, editConfirPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        iniComponents(v);
        configInterface();

        return v;
    }

    public void configInterface() {

        btnRegister.setOnClickListener(v -> validate());

    }

    public void validate() {

        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirm = editConfirPassword.getText().toString();

        if (!name.isEmpty()) {
            if (!email.isEmpty()) {
                if (email.contains("gmail") || email.contains("hotmail") || email.contains("outlook")) {
                    if (!password.isEmpty()) {
                        if (!confirm.isEmpty()) {
                            if (confirm.equals(password)) {

                                User u = new User();
                                u.setName(name);
                                u.setEmail(email);
                                u.setPassword(password);

                                registerUser(u);

                            } else {
                                snackBar("Ops! As senhas não batem confirme novamente.");
                            }
                        } else {
                            snackBar("Ops! Confirme sua senha.");
                        }
                    } else {
                        snackBar("Ops! Defina uma senha.");
                    }
                } else {
                    snackBar("Ops! Digite um e-mail do Gmail ou Hotmail");
                }
            } else {
                snackBar("Ops! Digite seu e-mail.");
            }
        } else {
            snackBar("Ops! Digite seu nome.");
        }

    }

    public void registerUser(User u) {

        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setText("CARREGANDO");

        auth.createUserWithEmailAndPassword(
                u.getEmail(),
                u.getPassword()).addOnCompleteListener(getActivity(),
                task -> {

                    try {
                        String id = task.getResult().getUser().getUid();
                        u.setId(id);
                        u.saveDataUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (task.isSuccessful()) {

                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        startActivity(i);

                    } else {

                        progressBar.setVisibility(View.GONE);
                        btnRegister.setText("CONTINUAR CADASTRO");
                        String message;

                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            message = "Ops! Por favor defina uma senha mais forte.";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            message = "Ops! Por favor defina um e-mail válido.";
                        } catch (FirebaseAuthUserCollisionException e) {
                            message = "Ops! Este endereço de e-mail já está cadastrado.";
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

        editName = v.findViewById(R.id.editNomeCadastro);
        editEmail = v.findViewById(R.id.editEmailCadastro);
        editPassword = v.findViewById(R.id.editSenhaCadastro);
        editConfirPassword = v.findViewById(R.id.editConfSenhaCadastro);
        btnRegister = v.findViewById(R.id.btnCadastrar);
        progressBar = v.findViewById(R.id.progressBarRegister);

        auth = ConfigFirebase.getAuth();
    }

}