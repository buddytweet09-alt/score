package com.footballscore.liveapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.footballscore.liveapp.databinding.FragmentMatchStatisticsBinding;
import com.footballscore.liveapp.network.ApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchStatisticsFragment extends Fragment {
    
    private FragmentMatchStatisticsBinding binding;
    private String matchId;
    private static final String ARG_MATCH_ID = "match_id";
    private static final String TAG = "MatchStatisticsFragment";

    public static MatchStatisticsFragment newInstance(String matchId) {
        MatchStatisticsFragment fragment = new MatchStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchId = getArguments().getString(ARG_MATCH_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMatchStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStatistics();
    }

    private void loadStatistics() {
        if (matchId == null) return;

        showLoading(true);
        
        ApiClient.getApiService().getMatchStatistics(matchId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    displayStatistics(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load statistics", t);
            }
        });
    }

    private void displayStatistics(JsonObject statsData) {
        try {
            JsonArray stats = statsData.getAsJsonArray("Stats");
            if (stats != null && stats.size() >= 2) {
                JsonObject homeStats = stats.get(0).getAsJsonObject();
                JsonObject awayStats = stats.get(1).getAsJsonObject();
                
                // Possession
                updateStatistic(binding.homePossession, binding.awayPossession, 
                    homeStats, awayStats, "Pss", "%");
                
                // Shots
                updateStatistic(binding.homeShots, binding.awayShots, 
                    homeStats, awayStats, "Ths", "");
                
                // Shots on Target
                updateStatistic(binding.homeShotsOnTarget, binding.awayShotsOnTarget, 
                    homeStats, awayStats, "Shon", "");
                
                // Corners
                updateStatistic(binding.homeCorners, binding.awayCorners, 
                    homeStats, awayStats, "Crs", "");
                
                // Fouls
                updateStatistic(binding.homeFouls, binding.awayFouls, 
                    homeStats, awayStats, "Fls", "");
                
                // Yellow Cards
                updateStatistic(binding.homeYellowCards, binding.awayYellowCards, 
                    homeStats, awayStats, "Ycs", "");
                
                // Red Cards
                updateStatistic(binding.homeRedCards, binding.awayRedCards, 
                    homeStats, awayStats, "Rcs", "");
                
                // Offsides
                updateStatistic(binding.homeOffsides, binding.awayOffsides, 
                    homeStats, awayStats, "Ofs", "");
            }
            
            binding.contentLayout.setVisibility(View.VISIBLE);
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing statistics", e);
            showError();
        }
    }

    private void updateStatistic(android.widget.TextView homeView, android.widget.TextView awayView,
                                JsonObject homeStats, JsonObject awayStats, String key, String suffix) {
        try {
            String homeValue = homeStats.has(key) ? homeStats.get(key).getAsString() : "0";
            String awayValue = awayStats.has(key) ? awayStats.get(key).getAsString() : "0";
            
            homeView.setText(homeValue + suffix);
            awayView.setText(awayValue + suffix);
        } catch (Exception e) {
            homeView.setText("0" + suffix);
            awayView.setText("0" + suffix);
        }
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.contentLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError() {
        binding.progressBar.setVisibility(View.GONE);
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}