package com.dinomobile.olxapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.activity.DetailsAdvertActivity;
import com.dinomobile.olxapp.adapter.AdapterAdverts;
import com.dinomobile.olxapp.config.ConfigFirebase;
import com.dinomobile.olxapp.config.UserFirebase;
import com.dinomobile.olxapp.helper.Data;
import com.dinomobile.olxapp.helper.RecyclerClick;
import com.dinomobile.olxapp.model.Advert;
import com.dinomobile.olxapp.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdvertsFragment extends Fragment {

    private static String idUser, stateLocalUser;
    private SearchView searchView;
    private TextView textCategoria;
    private FloatingActionButton fabSnackBar;
    private Button btnFilter;
    private FrameLayout snackBarFiltros;
    private RecyclerView recyclerAdverts;
    private AdapterAdverts adapter;
    private Spinner spinnerCategory, spinnerLocation;
    private final List<Advert> listAdverts = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference database;

    public AdvertsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adverts, container, false);

        iniComponents(v);
        configInteface();
        itemClick();
        animation(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {

            idUser = UserFirebase.getUserId();
            getDataUser();
            configSearch();
        } else {
            getAdverts("Paraíba (PB)");
        }

    }

    public void getDataUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idUser);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);
                    stateLocalUser = u.getStateLocation();

                    if (stateLocalUser != null) {

                        getAdverts(stateLocalUser);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configInteface() {

        ArrayAdapter<String> category = new ArrayAdapter<String>
                (getContext(), R.layout.spinner_item, Data.CATEGORY);
        category.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerCategory.setAdapter(category);

        ArrayAdapter<String> location = new ArrayAdapter<String>
                (getContext(), R.layout.spinner_item, Data.STATES_LOCATION);
        location.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLocation.setAdapter(location);

        btnFilter.setOnClickListener(v -> {

            filterAdvert();
        });

    }

    public void filterAdvert() {

        String stateLocalSelect = spinnerLocation.getSelectedItem().toString();
        String categorySelect = spinnerCategory.getSelectedItem().toString();

        DatabaseReference data = database.child("Adverts").child(stateLocalSelect);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listAdverts.clear();

                for (DataSnapshot states : snapshot.getChildren()) {
                    for (DataSnapshot category : states.getChildren()) {

                        Advert a = category.getValue(Advert.class);

                        if (a.getCategory().equals(categorySelect))
                            listAdverts.add(a);
                        textCategoria.setText(stateLocalSelect + " | " + categorySelect);

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void getAdverts(String local) {

        DatabaseReference adverts = database.child("Adverts").child(local);
        adverts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listAdverts.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot category : ds.getChildren()) {
                        Advert advert = category.getValue(Advert.class);

                        listAdverts.add(advert);

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configSearch() {

        searchView.setQueryHint("Busque por carros, roupas, brinquedos e etc.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            getAdverts(stateLocalUser);
            return true;
        });

    }

    public void search(String text) {

        String stateLocalUser = spinnerLocation.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        DatabaseReference search = database
                .child("Adverts")
                .child(stateLocalUser)
                .child(category);
        Query query = search.orderByChild("title").startAt(text).endAt("\uf8ff");

        if (text.length() >= 4 && !stateLocalUser.isEmpty() && !category.isEmpty()) {

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    listAdverts.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                            Advert advert = ds.getValue(Advert.class);

                            listAdverts.add(advert);

                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.d("ERROR", error.getMessage());
                }
            });

        } else {
            getAdverts(stateLocalUser);
        }

    }

    public void itemClick() {

        recyclerAdverts.addOnItemTouchListener(new RecyclerClick(getContext(),
                recyclerAdverts, new RecyclerClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Advert a = listAdverts.get(position);

                Intent i = new Intent(getContext(), DetailsAdvertActivity.class);
                i.putExtra("DATA_ADVERT", a);
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }

    public void animation(View v) {

        final ViewGroup root = v.findViewById(R.id.snackBarFiltros);

        fabSnackBar.setOnClickListener(btn -> {

            TransitionManager.beginDelayedTransition(root, new Slide());
            snackBarFiltros.setVisibility(snackBarFiltros.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);

        });

    }

    public void iniComponents(View v) {

        recyclerAdverts = v.findViewById(R.id.recyclerAnuncios);
        fabSnackBar = v.findViewById(R.id.fabSnackBarAnuncios);
        snackBarFiltros = v.findViewById(R.id.snackBarFiltros);
        spinnerCategory = v.findViewById(R.id.spinnerCategoria);
        spinnerLocation = v.findViewById(R.id.spinnerLocalizacao);
        textCategoria = v.findViewById(R.id.textStatusCategoria);
        searchView = v.findViewById(R.id.searchViewAdvert);
        btnFilter = v.findViewById(R.id.btnFiltrarAnuncios);

        adapter = new AdapterAdverts(getContext(), listAdverts);
        recyclerAdverts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdverts.setHasFixedSize(true);
        recyclerAdverts.setAdapter(adapter);

        auth = ConfigFirebase.getAuth();
        database = ConfigFirebase.getDatabase();
    }

}