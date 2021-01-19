package com.dinomobile.olxapp.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.adapter.AdapterPhotoSlide;
import com.dinomobile.olxapp.model.Advert;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdvertActivity extends AppCompatActivity {

    private Advert advert;
    private Button btnChat;
    private RecyclerView recyclerPhotos;
    private TextView nameUser, phone, title, dateHour, price, description, type, locale, stateProduct;
    private List<String> listURL = new ArrayList<>();
    private AdapterPhotoSlide adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_advert);

        iniComponents();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onStart() {
        super.onStart();
        configInterface();
    }

    public void configInterface() {

        nameUser.setText(advert.getNameUser());
        phone.setText(advert.getPhone());
        title.setText(advert.getTitle());
        price.setText(advert.getPrice());
        description.setText(advert.getDescription());
        dateHour.setText("Publicado em " + advert.getDateHour());
        type.setText("Tipo: " + advert.getCategory());
        stateProduct.setText("Estado do produto: " + advert.getState());
        locale.setText("Local: " + advert.getLocation());

        btnChat.setOnClickListener(v -> {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://api.whatsapp.com/send?phone=55" + advert.getPhone()));
            startActivity(i);

        });

        //  DatabaseReference image = database.child("Adverts").child(advert.getStateLocal()).child()

    }

    public void iniComponents() {

        recyclerPhotos = findViewById(R.id.recyclerFotosDetalhes);
        btnChat = findViewById(R.id.btnIniChatDetalhes);
        nameUser = findViewById(R.id.textNomeUsuarioDetalhes);
        phone = findViewById(R.id.textTelefoneDetalhes);
        title = findViewById(R.id.textTituloDetalhes);
        dateHour = findViewById(R.id.textDataDetalhes);
        price = findViewById(R.id.textPrecoDetalhes);
        description = findViewById(R.id.textDescricaoDetalhes);
        type = findViewById(R.id.textTipoDetalhes);
        stateProduct = findViewById(R.id.textEstadoPodutoDetalhes);
        locale = findViewById(R.id.textLocalDetalhes);

        Intent i = getIntent();
        advert = (Advert) i.getSerializableExtra("DATA_ADVERT");
        listURL = advert.getListUrlPhotos();

        adapter = new AdapterPhotoSlide(getApplicationContext(), listURL);
        recyclerPhotos.setHasFixedSize(true);
        recyclerPhotos.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerPhotos.setAdapter(adapter);

        getSupportActionBar().setTitle("Anúncio de " + advert.getNameUser());

    }

}