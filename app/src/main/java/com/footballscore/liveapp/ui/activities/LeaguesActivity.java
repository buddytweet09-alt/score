package com.footballscore.liveapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.footballscore.liveapp.databinding.ActivityLeaguesBinding;
import com.footballscore.liveapp.models.League;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.adapters.LeagueAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class LeaguesActivity extends AppCompatActivity implements LeagueAdapter.OnLeagueClickListener {
    
    private ActivityLeaguesBinding binding;
    private LeagueAdapter adapter;
    private static final String TAG = "LeaguesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaguesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupRecyclerView();
        loadLeagues();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Leagues & Competitions");
        }
    }

    private void setupRecyclerView() {
        adapter = new LeagueAdapter(new ArrayList<>(), this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadLeagues() {
        showLoading(true);
        
        ApiClient.getApiService().getPopularLeagues().enqueue(new Callback<List<League>>() {
            @Override
            public void onResponse(Call<List<League>> call, Response<List<League>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<League> leagues = response.body();
                    adapter.updateLeagues(leagues);
                    
                    if (leagues.isEmpty()) {
                        showEmptyState();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<League>> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load leagues", t);
            }
        });
    }

    @Override
    public void onLeagueClick(League league) {
        Intent intent = new Intent(this, LeagueTableActivity.class);
        intent.putExtra("country_code", league.getCountryCode());
        intent.putExtra("season_code", league.getSeasonCode());
        intent.putExtra("league_name", league.getName());
        startActivity(intent);
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError() {
        binding.progressBar.setVisibility(View.GONE);
        binding.errorLayout.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}