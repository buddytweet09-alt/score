package com.footballscore.liveapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ActivityMatchDetailsBinding;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.models.MatchDetails;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.fragments.MatchInfoFragment;
import com.footballscore.liveapp.ui.fragments.MatchLineupsFragment;
import com.footballscore.liveapp.ui.fragments.MatchStatisticsFragment;
import com.footballscore.liveapp.ui.fragments.MatchIncidentsFragment;
import com.footballscore.liveapp.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsActivity extends AppCompatActivity {
    
    private ActivityMatchDetailsBinding binding;
    private Match match;
    private String matchId;
    private static final String TAG = "MatchDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        getMatchData();
        setupViewPager();
        loadMatchDetails();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Match Details");
        }
    }

    private void getMatchData() {
        String matchJson = getIntent().getStringExtra("match");
        matchId = getIntent().getStringExtra("match_id");
        
        if (matchJson != null) {
            match = new Gson().fromJson(matchJson, Match.class);
            updateMatchHeader();
        } else if (matchId != null) {
            // Load match data from API if only ID is provided
            loadMatchInfo();
        }
    }

    private void updateMatchHeader() {
        if (match == null) return;

        // Set team names and scores
        binding.homeTeamName.setText(match.getHomeTeam().getName());
        binding.awayTeamName.setText(match.getAwayTeam().getName());
        binding.homeScore.setText(match.getHomeScore());
        binding.awayScore.setText(match.getAwayScore());

        // Load team logos
        Glide.with(this)
                .load(match.getHomeTeam().getLogo())
                .placeholder(R.drawable.ic_team_placeholder)
                .error(R.drawable.ic_team_placeholder)
                .into(binding.homeTeamLogo);

        Glide.with(this)
                .load(match.getAwayTeam().getLogo())
                .placeholder(R.drawable.ic_team_placeholder)
                .error(R.drawable.ic_team_placeholder)
                .into(binding.awayTeamLogo);

        // Set match status
        if (match.isLive()) {
            binding.matchStatus.setText(match.getMinute() + "' LIVE");
            binding.matchStatus.setTextColor(getColor(R.color.live_green));
        } else if (match.isFinished()) {
            binding.matchStatus.setText("FT");
            binding.matchStatus.setTextColor(getColor(R.color.finished_gray));
        } else {
            binding.matchStatus.setText(DateUtils.formatMatchTime(match.getStartTime()));
            binding.matchStatus.setTextColor(getColor(R.color.upcoming_blue));
        }

        // Set competition info
        if (match.getCompetition() != null) {
            binding.competitionName.setText(match.getCompetition().getName());
            Glide.with(this)
                    .load(match.getCompetition().getLogo())
                    .placeholder(R.drawable.ic_competition_placeholder)
                    .error(R.drawable.ic_competition_placeholder)
                    .into(binding.competitionLogo);
        }
    }

    private void setupViewPager() {
        MatchDetailsPagerAdapter adapter = new MatchDetailsPagerAdapter();
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Info");
                            break;
                        case 1:
                            tab.setText("Lineups");
                            break;
                        case 2:
                            tab.setText("Stats");
                            break;
                        case 3:
                            tab.setText("Events");
                            break;
                    }
                }).attach();
    }

    private void loadMatchDetails() {
        if (matchId == null && match != null) {
            matchId = match.getId();
        }
        
        if (matchId == null) {
            Toast.makeText(this, "Match ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // The fragments will handle their own API calls
        Log.d(TAG, "Match ID: " + matchId);
    }

    private void loadMatchInfo() {
        if (matchId == null) return;

        ApiClient.getApiService().getMatchInfo(matchId).enqueue(new Callback<MatchDetails>() {
            @Override
            public void onResponse(Call<MatchDetails> call, Response<MatchDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MatchDetails matchDetails = response.body();
                    // Update UI with match details
                    updateMatchInfoFromDetails(matchDetails);
                }
            }

            @Override
            public void onFailure(Call<MatchDetails> call, Throwable t) {
                Log.e(TAG, "Failed to load match info", t);
            }
        });
    }

    private void updateMatchInfoFromDetails(MatchDetails matchDetails) {
        // Update match header with details from API
        binding.matchStatus.setText(DateUtils.formatMatchTime(matchDetails.getStartDate()));
    }

    public String getMatchId() {
        return matchId;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class MatchDetailsPagerAdapter extends FragmentStateAdapter {
        public MatchDetailsPagerAdapter() {
            super(MatchDetailsActivity.this);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return MatchInfoFragment.newInstance(matchId);
                case 1:
                    return MatchLineupsFragment.newInstance(matchId);
                case 2:
                    return MatchStatisticsFragment.newInstance(matchId);
                case 3:
                    return MatchIncidentsFragment.newInstance(matchId);
                default:
                    return MatchInfoFragment.newInstance(matchId);
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}