package com.dinomobile.olxapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.activity.MainActivity;
import com.dinomobile.olxapp.adapter.AdapterSelectPhoto;
import com.dinomobile.olxapp.config.ConfigFirebase;
import com.dinomobile.olxapp.config.UserFirebase;
import com.dinomobile.olxapp.helper.CurrentTimeDate;
import com.dinomobile.olxapp.helper.Data;
import com.dinomobile.olxapp.helper.RecyclerClick;
import com.dinomobile.olxapp.model.Advert;
import com.dinomobile.olxapp.model.Photo;
import com.dinomobile.olxapp.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InsertAdvertsFragment extends Fragment {

    private static final int SELECT_GALERY = 200;
    private final List<Photo> listPhoto = new ArrayList<>();
    private final List<String> listUrl = new ArrayList<>();
    private final Locale locale = new Locale("pt", "BR");
    private View insertAdvertsFragment;
    private EditText editTitle, editDescription, editCep;
    private CurrencyEditText editPrice;
    private RecyclerView recyclerPhotos;
    private RadioButton radioNew, radioUsed;
    private Spinner spinner;
    private Button btnSendAdvert, btnSelectPhoto;
    private FirebaseAuth auth;
    private StorageReference storage;
    private DatabaseReference database;
    private AdapterSelectPhoto adapterPhoto;
    private String idUser, nameUser, phone, title, description, cep, category, state, stateLocation, dateHour, price;

    public InsertAdvertsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_insert_adverts, container, false);

        iniComponents(v);
        configInterface();
        itemClick();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {
            idUser = UserFirebase.getUserId();
            getDataUser();
        } else {
            insertAdvertsFragment.setVisibility(View.INVISIBLE);
            UserFirebase.statusAuth(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Bitmap image = null;

            try {

                if (requestCode == SELECT_GALERY) {

                    Uri localeImage = data.getData();
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localeImage);
                }

                if (image != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] imageBytes = baos.toByteArray();

                    Photo photo = new Photo();
                    photo.setBytes(imageBytes);
                    photo.setBitmap(image);

                    animation();

                    listPhoto.add(photo);
                    adapterPhoto.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void getDataUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idUser);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);

                    nameUser = u.getName();
                    phone = u.getPhone();
                    cep = u.getCep();
                    stateLocation = u.getStateLocation();
                    editCep.setText(u.getCep());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void sendAdverts() {

        btnSendAdvert.setText("CARREGANDO");
        btnSendAdvert.setClickable(false);
        String idAdvert = database.push().getKey();

        for (Photo photo : listPhoto) {

            String idPhoto = database.push().getKey();
            StorageReference image = storage.child("Images").child(category)
                    .child("Adverts").child(idAdvert).child(idPhoto + ".JPEG");

            UploadTask upload = image.putBytes(photo.getBytes());
            upload.addOnSuccessListener(taskSnapshot -> {

                image.getDownloadUrl().addOnCompleteListener(task -> {

                    String url = task.getResult().toString();
                    listUrl.add(url);

                    Advert a = new Advert();
                    a.setIdUser(idUser);
                    a.setCategory(category);
                    a.setNameUser(nameUser);
                    a.setPhone(phone);
                    a.setId(idAdvert);
                    a.setTitle(title);
                    a.setState(state);
                    a.setDateHour(dateHour);
                    a.setDescription(description);
                    a.setCep(cep);
                    a.setLocation(stateLocation + " - " + cep);
                    a.setStateLocal(stateLocation);
                    a.setListUrlPhotos(listUrl);
                    a.setPrice(price);
                    a.sendAdvert();

                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);

                });

            });
        }

    }

    public void configInterface() {

        // Config Moeda Local Preço
        editPrice.setLocale(locale);

        btnSelectPhoto.setOnClickListener(v -> {

            if (listPhoto.size() < 5) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (getActivity().getPackageManager() != null) {
                    startActivityForResult(i, SELECT_GALERY);
                }

            } else {
                snackBar("Você só pode selecionar 5 fotos.");
            }

        });

        btnSendAdvert.setOnClickListener(v -> {

            validateAdvert();

        });

    }

    public void itemClick() {

        recyclerPhotos.addOnItemTouchListener(new RecyclerClick(getContext(),
                recyclerPhotos, new RecyclerClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

                Photo p = listPhoto.get(position);
                listPhoto.remove(p);
                adapterPhoto.notifyDataSetChanged();

                animation();
                snackBar("Foto Removida.");
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }

    public void validateAdvert() {

        category = spinner.getSelectedItem().toString();
        title = editTitle.getText().toString();
        description = editDescription.getText().toString();
        cep = editCep.getText().toString();
        price = editPrice.getText().toString();

        if (radioUsed.isChecked()) {
            state = "Novo";
        } else if (radioNew.isChecked()) {
            state = "Usado";
        }

        if (!title.isEmpty()) {
            if (!description.isEmpty()) {
                if (!cep.isEmpty()) {
                    if (!category.isEmpty()) {
                        if (listPhoto.size() > 0) {

                            Snackbar.make(getView(), "Deseja adicionar este anúncio?",
                                    BaseTransientBottomBar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.white))
                                    .setTextColor(getResources().getColor(R.color.colorText))
                                    .setAction("CONTINUAR", v -> {

                                        sendAdverts();

                                    }).show();

                        } else {
                            snackBar("Selecione fotos para o anúncio!");
                        }
                    } else {
                        snackBar("Defina uma categoria!");
                    }
                } else {
                    snackBar("Qual seu Cep?");
                }
            } else {
                snackBar("Defina uma descrição!");
            }
        } else {
            snackBar("Defina o titulo do produto!");
        }

    }

    public void snackBar(String message) {

        Snackbar.make(getView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.white))
                .setTextColor(getResources().getColor(R.color.colorText))
                .show();
    }

    public void animation() {

        ViewGroup viewGroup = getView().findViewById(R.id.recyclerFotosAnuncio);

        TransitionManager.beginDelayedTransition(viewGroup, new Fade());
        recyclerPhotos.setVisibility(View.INVISIBLE);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                recyclerPhotos.setVisibility(View.VISIBLE);

            }
        }, 1000);

    }

    public void iniComponents(View v) {

        insertAdvertsFragment = v.findViewById(R.id.inserirAnuncioFragment);
        editTitle = v.findViewById(R.id.editTituloAnuncio);
        editDescription = v.findViewById(R.id.editDescricaoAnuncio);
        editCep = v.findViewById(R.id.editCepAnuncio);
        editPrice = v.findViewById(R.id.editPrecoAnuncio);
        recyclerPhotos = v.findViewById(R.id.recyclerFotosAnuncio);
        radioNew = v.findViewById(R.id.radioNovo);
        radioUsed = v.findViewById(R.id.radioUsado);
        spinner = v.findViewById(R.id.spinnerCategoriaAnuncio);
        btnSendAdvert = v.findViewById(R.id.btnEnviarAnuncio);
        btnSelectPhoto = v.findViewById(R.id.btnSelectPhotoAdvert);

        adapterPhoto = new AdapterSelectPhoto(getContext(), listPhoto);
        recyclerPhotos.setHasFixedSize(true);
        recyclerPhotos.setLayoutManager(new LinearLayoutManager(
                getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerPhotos.setAdapter(adapterPhoto);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>
                (getContext(), R.layout.spinner_item, Data.CATEGORY);
        adapterCategory.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapterCategory);

        auth = ConfigFirebase.getAuth();
        database = ConfigFirebase.getDatabase();
        storage = ConfigFirebase.getStorage();
        dateHour = CurrentTimeDate.dateHour();
    }

}