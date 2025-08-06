package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MatchDetails {
    @SerializedName("Eid")
    private String matchId;
    
    @SerializedName("Esd")
    private long startDate;
    
    @SerializedName("Vnm")
    private String venueName;
    
    @SerializedName("VCnm")
    private String venueCountry;
    
    @SerializedName("Vcy")
    private String venueCity;
    
    @SerializedName("Refs")
    private List<Referee> referees;

    // Getters and Setters
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCountry() {
        return venueCountry;
    }

    public void setVenueCountry(String venueCountry) {
        this.venueCountry = venueCountry;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    public static class Referee {
        @SerializedName("Nm")
        private String name;
        
        @SerializedName("Cid")
        private String countryId;
        
        @SerializedName("Cn")
        private String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}