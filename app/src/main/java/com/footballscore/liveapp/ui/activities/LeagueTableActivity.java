package com.footballscore.liveapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.footballscore.liveapp.databinding.ActivityLeagueTableBinding;
import com.footballscore.liveapp.models.LeagueTable;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.adapters.LeagueTableAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class LeagueTableActivity extends AppCompatActivity {
    
    private ActivityLeagueTableBinding binding;
    private LeagueTableAdapter adapter;
    private String countryCode;
    private String seasonCode;
    private String leagueName;
    private static final String TAG = "LeagueTableActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeagueTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentData();
        setupToolbar();
        setupRecyclerView();
        loadLeagueTable();
    }

    private void getIntentData() {
        countryCode = getIntent().getStringExtra("country_code");
        seasonCode = getIntent().getStringExtra("season_code");
        leagueName = getIntent().getStringExtra("league_name");
        
        if (countryCode == null) countryCode = "england";
        if (seasonCode == null) seasonCode = "premier-league";
        if (leagueName == null) leagueName = "League Table";
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(leagueName);
        }
    }

    private void setupRecyclerView() {
        adapter = new LeagueTableAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadLeagueTable() {
        showLoading(true);
        
        // Use default values for testing
        String cCode = countryCode != null ? countryCode : "england";
        String sCode = seasonCode != null ? seasonCode : "premier-league";
        
        Log.d(TAG, "Loading league table for: " + cCode + "/" + sCode);
        
        ApiClient.getApiService().getLeagueTable(cCode, sCode).enqueue(new Callback<List<LeagueTable>>() {
            @Override
            public void onResponse(Call<List<LeagueTable>> call, Response<List<LeagueTable>> response) {
                showLoading(false);
                Log.d(TAG, "League table response: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<LeagueTable> table = response.body();
                    Log.d(TAG, "Received " + table.size() + " teams");
                    adapter.updateTable(table);
                    
                    if (table.isEmpty()) {
                        showEmptyState();
                    }
                } else {
                    Log.e(TAG, "League table error: " + response.message());
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<LeagueTable>> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load league table", t);
            }
        });
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