package com.dinomobile.olxapp.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.config.ConfigFirebase;
import com.dinomobile.olxapp.config.UserFirebase;
import com.dinomobile.olxapp.helper.Data;
import com.dinomobile.olxapp.helper.Permission;
import com.dinomobile.olxapp.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskara.widget.MaskEditText;

import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private View profileFragment;
    private TextView textLocation;
    private EditText editName, editEmail, editCep;
    private MaskEditText editPhone;
    private Button btnSave, btnMyLocation;
    private Spinner spinner;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private String idUser;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        iniComponents(v);
        configInterface();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {

            idUser = UserFirebase.getUserId();
            getDataUser();
        } else {
            profileFragment.setVisibility(View.INVISIBLE);
            UserFirebase.statusAuth(getActivity());
        }
    }

    public void getDataUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idUser);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);

                    editName.setText(u.getName());
                    editEmail.setText(u.getEmail());
                    editPhone.setText(u.getPhone());
                    editCep.setText(u.getCep());
                    textLocation.setText(u.getStateLocation() + " - " + u.getCep());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configInterface() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Data.STATES_LOCATION);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {

            Snackbar.make(v, "Deseja salvar as alterações?",
                    BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("CONFIRMAR", v1 -> {

                        saveDatas();

                    }).show();

        });

        btnMyLocation.setOnClickListener(v -> {

            getLocation();

        });

    }

    public void getLocation() {


        String cep = editCep.getText().toString();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {

            List<Address> list = geocoder.getFromLocationName(cep, 1);

            for (Address address : list) {

                list.add(address);

                StringBuilder sb = new StringBuilder();
                sb.append(list.get(1).getAdminArea());

                textLocation.setText(sb);
                Log.i("ADDRESS", sb.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveDatas() {

        String name = editName.getText().toString();
        String cep = editCep.getText().toString();
        String phone = editPhone.getUnMasked();
        String stateLocation = spinner.getSelectedItem().toString();

        if (!name.isEmpty()) {
            if (!phone.isEmpty()) {
                if (!cep.isEmpty()) {
                    if (!stateLocation.isEmpty()) {


                        User u = new User();
                        u.setId(idUser);
                        u.setName(name);
                        u.setCep(cep);
                        u.setStateLocation(stateLocation);
                        u.setPhone(phone);

                        u.updateDataUser();

                        snackBar("Dados atualizados com sucesso!");

                    } else {
                        snackBar("Escolha um estado!");
                    }
                } else {
                    snackBar("Digite seu cep!");
                }
            } else {
                snackBar("Digite seu telefone!");
            }
        } else {
            snackBar("Digite seu nome!");
        }

    }

    public void snackBar(String message) {

        Snackbar.make(getView(), message, BaseTransientBottomBar
                .LENGTH_LONG).show();

    }

    public void iniComponents(View v) {

        profileFragment = v.findViewById(R.id.perfilUsuarioFragment);
        spinner = v.findViewById(R.id.spinnerEstadoPerfil);
        textLocation = v.findViewById(R.id.textMeuLocalPerfil);
        editName = v.findViewById(R.id.editNomePerfil);
        editEmail = v.findViewById(R.id.editEmailPerfil);
        editPhone = v.findViewById(R.id.editTelefonePerfil);
        editCep = v.findViewById(R.id.editCepPerfil);
        btnMyLocation = v.findViewById(R.id.btnMeuLocalPerfil);
        btnSave = v.findViewById(R.id.btnSalvarPerfil);

        auth = ConfigFirebase.getAuth();
        database = ConfigFirebase.getDatabase();

        Permission.validatePermissions(Permission.PERMISSIONS, getActivity(), 1);

    }

}