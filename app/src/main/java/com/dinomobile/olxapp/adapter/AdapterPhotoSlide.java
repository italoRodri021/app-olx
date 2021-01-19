package com.dinomobile.olxapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dinomobile.olxapp.R;

import java.util.List;

public class AdapterPhotoSlide extends RecyclerView.Adapter<AdapterPhotoSlide.MyViewHolder> {

    private final Context context;
    private final List<String> listUrl;

    public AdapterPhotoSlide(Context context, List<String> listUrl) {

        this.context = context;
        this.listUrl = listUrl;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_select_photos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String photo = listUrl.get(position);

        Uri url = Uri.parse(photo);
        Glide.with(context).load(url).into(holder.imagePhoto);

    }

    @Override
    public int getItemCount() {
        return listUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageAdpterSelecaoFoto);
        }

    }

}
