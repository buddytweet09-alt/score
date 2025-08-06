package com.footballscore.liveapp.models;

public class Incident {
    private String minute;
    private int type;
    private String playerName;
    private int teamNumber;
    private String description;

    // Incident types
    public static final int TYPE_GOAL = 36;
    public static final int TYPE_YELLOW_CARD = 17;
    public static final int TYPE_RED_CARD = 18;
    public static final int TYPE_SUBSTITUTION = 19;
    public static final int TYPE_PENALTY = 41;

    public Incident() {}

    // Getters and Setters
    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeDescription() {
        switch (type) {
            case TYPE_GOAL:
                return "Goal";
            case TYPE_YELLOW_CARD:
                return "Yellow Card";
            case TYPE_RED_CARD:
                return "Red Card";
            case TYPE_SUBSTITUTION:
                return "Substitution";
            case TYPE_PENALTY:
                return "Penalty";
            default:
                return "Event";
        }
    }

    public boolean isGoal() {
        return type == TYPE_GOAL;
    }

    public boolean isCard() {
        return type == TYPE_YELLOW_CARD || type == TYPE_RED_CARD;
    }

    public boolean isSubstitution() {
        return type == TYPE_SUBSTITUTION;
    }
}