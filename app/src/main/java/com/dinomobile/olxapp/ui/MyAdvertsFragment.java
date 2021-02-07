package com.dinomobile.olxapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.adapter.AdapterAdverts;
import com.dinomobile.olxapp.config.ConfigFirebase;
import com.dinomobile.olxapp.config.UserFirebase;
import com.dinomobile.olxapp.model.Advert;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyAdvertsFragment extends Fragment {

    private View advertsFragment;
    private RecyclerView recyclerAdverts;
    private AdapterAdverts adapter;
    private final List<Advert> advertList = new ArrayList<>();
    private String idUser;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private StorageReference storage;
    private AlertDialog.Builder alertDialog;

    public MyAdvertsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_adverts, container, false);

        iniComponents(v);
        swipe();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (auth.getCurrentUser() != null) {

            idUser = UserFirebase.getUserId();
            getAdverts();
        } else {
            advertsFragment.setVisibility(View.INVISIBLE);
            UserFirebase.statusAuth(getActivity());
        }

    }

    public void getAdverts() {

        DatabaseReference adverts = database.child("User").child("Profile")
                .child(idUser).child("Adverts");
        adverts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Advert advert = ds.getValue(Advert.class);

                    advertList.add(advert);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView rV,
                                        @NonNull RecyclerView.ViewHolder vH) {
                int DRAG = ItemTouchHelper.ACTION_STATE_IDLE;
                int SWIPE = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(DRAG, SWIPE);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView rV,
                                  @NonNull RecyclerView.ViewHolder vH,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder
                                         viewHolder, int direction) {
                int positon = viewHolder.getAdapterPosition();

                Advert a = advertList.get(positon);
                deleteAdvert(a.getId());

            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerAdverts);

    }

    public void deleteAdvert(String idAdvert) {

        alertDialog.setTitle("Deseja excluir este anúncio")
                .setMessage("Está ação não pode ser desfeita")
                .setCancelable(false)
                .setPositiveButton("CONFIRMAR",
                        (dialog, which) -> {

                            DatabaseReference advert = database
                                    .child("User").child("Profile")
                                    .child(idUser).child("Adverts");
                            advert.child(idAdvert).removeValue();

                            StorageReference photo = storage.child("Images")
                                    .child("Adverts").child(idAdvert)
                                    .child(idAdvert + ".JPEG");
                            photo.delete();
                            adapter.notifyDataSetChanged();

                            snackBar("Anúncio excluido com sucesso!");

                        }).setNegativeButton("CANCELAR",
                (dialog, which) -> {

                    adapter.notifyDataSetChanged();
                }).create().show();

    }

    public void snackBar(String message) {

        Snackbar.make(getView(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.white))
                .setTextColor(getResources().getColor(R.color.colorText))
                .show();

    }

    public void iniComponents(View v) {

        advertsFragment = v.findViewById(R.id.meusAnunciosFragment);
        recyclerAdverts = v.findViewById(R.id.recyclerMeusAnuncios);
        alertDialog = new AlertDialog.Builder(getContext());

        adapter = new AdapterAdverts(getContext(), advertList);
        recyclerAdverts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdverts.setHasFixedSize(true);
        recyclerAdverts.setAdapter(adapter);

        auth = ConfigFirebase.getAuth();
        database = ConfigFirebase.getDatabase();
        storage = ConfigFirebase.getStorage();

    }

}