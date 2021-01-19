package com.dinomobile.olxapp.model;

import android.graphics.Bitmap;

public class Photo {

    private String id;
    private String url;
    private byte[] Bytes;
    private Bitmap bitmap;

    public Photo() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBytes() {
        return Bytes;
    }

    public void setBytes(byte[] bytes) {
        Bytes = bytes;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
