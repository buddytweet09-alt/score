package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;

public class LeagueTable {
    @SerializedName("ID")
    private String teamId;

    @SerializedName("Nm")
    private String teamName;

    @SerializedName("Img")
    private String teamLogo;

    @SerializedName("Pos")
    private int position;

    @SerializedName("Pts")
    private int points;

    @SerializedName("Pld")
    private int played;

    @SerializedName("W")
    private int wins;

    @SerializedName("D")
    private int draws;

    @SerializedName("L")
    private int losses;

    @SerializedName("GF")
    private int goalsFor;

    @SerializedName("GA")
    private int goalsAgainst;

    @SerializedName("GD")
    private int goalDifference;

    // Constructors
    public LeagueTable() {}

    // Getters and Setters
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }
}