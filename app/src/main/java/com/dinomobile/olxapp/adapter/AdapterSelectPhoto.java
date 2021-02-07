package com.dinomobile.olxapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dinomobile.olxapp.R;
import com.dinomobile.olxapp.model.Photo;

import java.util.List;

public class AdapterSelectPhoto extends RecyclerView.Adapter<AdapterSelectPhoto.MyViewHolder> {

    private final Context context;
    private final List<Photo> listPhoto;

    public AdapterSelectPhoto(Context context, List<Photo> listPhoto) {

        this.context = context;
        this.listPhoto = listPhoto;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_select_photos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Photo p = listPhoto.get(position);
        holder.imagePhoto.setImageBitmap(p.getBitmap());

    }

    @Override
    public int getItemCount() {
        return listPhoto.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageAdpterSelecaoFoto);

        }
    }

}
