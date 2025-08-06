package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;

public class Match {
    @SerializedName("Eid")
    private String id;

    @SerializedName("Tr1")
    private String homeScore;

    @SerializedName("Tr2")
    private String awayScore;

    @SerializedName("T1")
    private Team homeTeam;

    @SerializedName("T2")
    private Team awayTeam;

    @SerializedName("Eps")
    private String status;

    @SerializedName("Esd")
    private long startTime;

    @SerializedName("Ecov")
    private int coverage;

    @SerializedName("Comp")
    private Competition competition;

    @SerializedName("Minute")
    private String minute;

    private boolean isFavorite;

    // Constructors
    public Match() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeScore() {
        return homeScore != null ? homeScore : "0";
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getAwayScore() {
        return awayScore != null ? awayScore : "0";
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getCoverage() {
        return coverage;
    }

    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isLive() {
        return "1".equals(status) || "2".equals(status);
    }

    public boolean isFinished() {
        return "3".equals(status);
    }

    public boolean isUpcoming() {
        return "0".equals(status);
    }

    // Custom deserializer to handle API response variations
    public static class MatchDeserializer implements JsonDeserializer<Match> {
        @Override
        public Match deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Match match = new Match();
            
            try {
                JsonObject jsonObject = json.getAsJsonObject();
                
                // Basic match info
                if (jsonObject.has("Eid")) {
                    match.setId(jsonObject.get("Eid").getAsString());
                }
                
                if (jsonObject.has("Tr1") && !jsonObject.get("Tr1").isJsonNull()) {
                    match.setHomeScore(jsonObject.get("Tr1").getAsString());
                }
                
                if (jsonObject.has("Tr2") && !jsonObject.get("Tr2").isJsonNull()) {
                    match.setAwayScore(jsonObject.get("Tr2").getAsString());
                }
                
                if (jsonObject.has("Eps")) {
                    match.setStatus(jsonObject.get("Eps").getAsString());
                }
                
                if (jsonObject.has("Esd")) {
                    match.setStartTime(jsonObject.get("Esd").getAsLong());
                }
                
                if (jsonObject.has("Ecov")) {
                    match.setCoverage(jsonObject.get("Ecov").getAsInt());
                }
                
                if (jsonObject.has("Minute") && !jsonObject.get("Minute").isJsonNull()) {
                    match.setMinute(jsonObject.get("Minute").getAsString());
                }
                
                // Handle teams - they might be objects or arrays
                if (jsonObject.has("T1")) {
                    JsonElement t1Element = jsonObject.get("T1");
                    if (t1Element.isJsonObject()) {
                        Team homeTeam = context.deserialize(t1Element, Team.class);
                        match.setHomeTeam(homeTeam);
                    } else if (t1Element.isJsonArray() && t1Element.getAsJsonArray().size() > 0) {
                        Team homeTeam = context.deserialize(t1Element.getAsJsonArray().get(0), Team.class);
                        match.setHomeTeam(homeTeam);
                    }
                }
                
                if (jsonObject.has("T2")) {
                    JsonElement t2Element = jsonObject.get("T2");
                    if (t2Element.isJsonObject()) {
                        Team awayTeam = context.deserialize(t2Element, Team.class);
                        match.setAwayTeam(awayTeam);
                    } else if (t2Element.isJsonArray() && t2Element.getAsJsonArray().size() > 0) {
                        Team awayTeam = context.deserialize(t2Element.getAsJsonArray().get(0), Team.class);
                        match.setAwayTeam(awayTeam);
                    }
                }
                
                // Handle competition
                if (jsonObject.has("Comp")) {
                    Competition competition = context.deserialize(jsonObject.get("Comp"), Competition.class);
                    match.setCompetition(competition);
                }
                
            } catch (Exception e) {
                // Return null for invalid matches
                return null;
            }
            
            return match;
        }
    }
}