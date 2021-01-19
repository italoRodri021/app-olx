package com.dinomobile.olxapp.model;

import com.dinomobile.olxapp.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String cep;
    private String stateLocation;
    private String password;

    public void saveDataUser() {
        DatabaseReference database = ConfigFirebase.getDatabase()
                .child("User")
                .child("Profile")
                .child(getId());

        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", getName());
        map.put("phone", getPhone());
        map.put("email", getEmail());
        map.put("password", getPassword());
        database.updateChildren(map);

    }

    public void updateDataUser() {

        DatabaseReference database = ConfigFirebase.getDatabase()
                .child("User")
                .child("Profile")
                .child(getId());

        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", getName());
        map.put("phone", getPhone());
        map.put("stateLocation", getStateLocation());
        map.put("cep", getCep());
        database.updateChildren(map);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getStateLocation() {
        return stateLocation;
    }

    public void setStateLocation(String stateLocation) {
        this.stateLocation = stateLocation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
