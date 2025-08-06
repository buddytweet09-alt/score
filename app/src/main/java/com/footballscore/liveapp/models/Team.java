package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;

public class Team {
    @SerializedName("ID")
    private String id;

    @SerializedName("Nm")
    private String name;

    @SerializedName("Img")
    private String logo;

    @SerializedName("Abr")
    private String abbreviation;

    // Constructors
    public Team() {}

    public Team(String id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}