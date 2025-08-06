package com.footballscore.liveapp.network;

import com.footballscore.liveapp.models.MatchResponse;
import com.footballscore.liveapp.models.MatchDetails;
import com.footballscore.liveapp.models.Player;
import com.footballscore.liveapp.models.League;
import com.footballscore.liveapp.models.LeagueTable;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("matches/list-live")
    Call<MatchResponse> getLiveMatches(@Query("category") String category);

    @GET("matches/list-by-date")
    Call<MatchResponse> getMatchesByDate(
        @Query("category") String category,
        @Query("date") String date
    );

    @GET("matches/get-scoreboard")
    Call<JsonObject> getMatchScoreboard(@Query("eid") String matchId);

    @GET("matches/get-h2h")
    Call<JsonObject> getMatchH2H(@Query("eid") String matchId);

    @GET("matches/get-info")
    Call<MatchDetails> getMatchInfo(@Query("eid") String matchId);

    @GET("matches/get-lineups")
    Call<List<Player>> getMatchLineups(@Query("eid") String matchId);

    @GET("matches/get-statistics")
    Call<JsonObject> getMatchStatistics(@Query("eid") String matchId);

    @GET("matches/get-incidents")
    Call<JsonObject> getMatchIncidents(@Query("eid") String matchId);

    @GET("leagues/list-popular")
    Call<List<League>> getPopularLeagues();

    @GET("leagues/get-table")
    Call<List<LeagueTable>> getLeagueTable(
        @Query("ccd") String countryCode,
        @Query("scd") String seasonCode
    );
}