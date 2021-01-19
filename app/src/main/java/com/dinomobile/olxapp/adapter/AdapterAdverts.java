package com.dinomobile.olxapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.model.Advert;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterAdverts extends RecyclerView.Adapter<AdapterAdverts.MyViewHolder> {

    private final Context context;
    private final List<Advert> listAdverts;
    private final DecimalFormat d = new DecimalFormat("00.00");

    public AdapterAdverts(Context context, List<Advert> listAdverts) {
        this.context = context;
        this.listAdverts = listAdverts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_adverts, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Advert a = listAdverts.get(position);

        holder.title.setText(a.getTitle());
        holder.price.setText(a.getPrice());
        holder.locale.setText(a.getLocation());

        String index = a.getListUrlPhotos().get(0);
        Uri URI = Uri.parse(index);
        Glide.with(context).load(URI).into(holder.imagePhoto);

    }

    @Override
    public int getItemCount() {
        return listAdverts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;
        TextView title, locale, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageFotoAnuncioAdapter);
            title = itemView.findViewById(R.id.textTituloAnuncioAdapter);
            price = itemView.findViewById(R.id.textPrecoAnuncioAdapter);
            locale = itemView.findViewById(R.id.textLocalAnuncioAdapter);

        }

    }


}
