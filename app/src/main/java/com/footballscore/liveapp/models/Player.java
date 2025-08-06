package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;

public class Player {
    @SerializedName("Aid")
    private String id;

    @SerializedName("Pid")
    private String playerId;

    @SerializedName("Fn")
    private String firstName;

    @SerializedName("Ln")
    private String lastName;

    @SerializedName("Pnt")
    private String playerName;

    @SerializedName("Pos")
    private int position;

    @SerializedName("Pon")
    private String positionName;

    @SerializedName("Snu")
    private int shirtNumber;

    @SerializedName("Rate")
    private String rating;

    @SerializedName("Tnb")
    private int teamNumber;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}