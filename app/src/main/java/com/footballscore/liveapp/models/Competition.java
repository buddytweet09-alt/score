package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;

public class Competition {
    @SerializedName("ID")
    private String id;

    @SerializedName("Nm")
    private String name;

    @SerializedName("Img")
    private String logo;

    @SerializedName("Cnm")
    private String country;

    // Constructors
    public Competition() {}

    // Getters and Setters
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}