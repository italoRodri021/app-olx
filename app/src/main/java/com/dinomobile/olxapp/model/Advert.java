package com.dinomobile.olxapp.model;

import com.dinomobile.olxapp.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Advert implements Serializable {

    private String id;
    private String dateHour;
    private String idUser;
    private String nameUser;
    private String title;
    private String description;
    private String category;
    private String email;
    private String phone;
    private String cep;
    private String price;
    private String state;
    private String location;
    private String stateLocal;
    private List<String> listUrlPhotos;

    public Advert() {

    }

    public void sendAdvert() {

        DatabaseReference user = ConfigFirebase.getDatabase()
                .child("User")
                .child("Profile")
                .child(getIdUser())
                .child("Adverts")
                .child(getId());
        DatabaseReference adverts = ConfigFirebase.getDatabase()
                .child("Adverts")
                .child(getStateLocal())
                .child(getCategory())
                .child(getId());


        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("nameUser", getNameUser());
        map.put("title", getTitle());
        map.put("description", getDescription());
        map.put("category", getCategory());
        map.put("email", getEmail());
        map.put("phone", getPhone());
        map.put("cep", getCep());
        map.put("price", getPrice());
        map.put("state", getState());
        map.put("location", getLocation());
        map.put("dateHour", getDateHour());
        map.put("stateLocal", getStateLocal());
        map.put("listUrlPhotos", getListUrlPhotos());

        user.updateChildren(map);
        adverts.updateChildren(map);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateHour() {
        return dateHour;
    }

    public void setDateHour(String dateHour) {
        this.dateHour = dateHour;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStateLocal() {
        return stateLocal;
    }

    public void setStateLocal(String stateLocal) {
        this.stateLocal = stateLocal;
    }

    public List<String> getListUrlPhotos() {
        return listUrlPhotos;
    }

    public void setListUrlPhotos(List<String> listUrlPhotos) {
        this.listUrlPhotos = listUrlPhotos;
    }
}
