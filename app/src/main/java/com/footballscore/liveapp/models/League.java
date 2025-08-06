package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;

public class League {
    @SerializedName("ID")
    private String id;

    @SerializedName("Nm")
    private String name;

    @SerializedName("Img")
    private String logo;

    @SerializedName("Cnm")
    private String country;

    @SerializedName("Ccd")
    private String countryCode;

    @SerializedName("Scd")
    private String seasonCode;

    // Constructors
    public League() {}

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
        this.seasonCode = seasonCode;
    }
}