package com.footballscore.liveapp.models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class MatchResponse {
    @SerializedName("Stages")
    private List<Stage> stages;

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public static class Stage {
        @SerializedName("Snm")
        private String name;
        
        @SerializedName("Events")
        private List<Match> matches;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Match> getMatches() {
            return matches;
        }

        public void setMatches(List<Match> matches) {
            this.matches = matches;
        }
    }

    // Custom deserializer to handle API response variations
    public static class MatchResponseDeserializer implements JsonDeserializer<MatchResponse> {
        @Override
        public MatchResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            MatchResponse response = new MatchResponse();
            List<Stage> stages = new ArrayList<>();
            
            try {
                JsonObject jsonObject = json.getAsJsonObject();
                
                if (jsonObject.has("Stages") && jsonObject.get("Stages").isJsonArray()) {
                    JsonArray stagesArray = jsonObject.getAsJsonArray("Stages");
                    
                    for (JsonElement stageElement : stagesArray) {
                        JsonObject stageObject = stageElement.getAsJsonObject();
                        Stage stage = new Stage();
                        
                        if (stageObject.has("Snm")) {
                            stage.setName(stageObject.get("Snm").getAsString());
                        }
                        
                        if (stageObject.has("Events") && stageObject.get("Events").isJsonArray()) {
                            JsonArray eventsArray = stageObject.getAsJsonArray("Events");
                            List<Match> matches = new ArrayList<>();
                            
                            for (JsonElement eventElement : eventsArray) {
                                try {
                                    Match match = context.deserialize(eventElement, Match.class);
                                    if (match != null) {
                                        matches.add(match);
                                    }
                                } catch (Exception e) {
                                    // Skip invalid match data
                                    continue;
                                }
                            }
                            
                            stage.setMatches(matches);
                        }
                        
                        stages.add(stage);
                    }
                }
                
                response.setStages(stages);
                
            } catch (Exception e) {
                // Return empty response if parsing fails
                response.setStages(new ArrayList<>());
            }
            
            return response;
        }
    }
}