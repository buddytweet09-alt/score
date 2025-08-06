package com.footballscore.liveapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.footballscore.liveapp.databinding.FragmentMatchLineupsBinding;
import com.footballscore.liveapp.models.Player;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.adapters.LineupAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MatchLineupsFragment extends Fragment {
    
    private FragmentMatchLineupsBinding binding;
    private String matchId;
    private LineupAdapter homeLineupAdapter;
    private LineupAdapter awayLineupAdapter;
    private static final String ARG_MATCH_ID = "match_id";
    private static final String TAG = "MatchLineupsFragment";

    public static MatchLineupsFragment newInstance(String matchId) {
        MatchLineupsFragment fragment = new MatchLineupsFragment();
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
        binding = FragmentMatchLineupsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews();
        loadLineups();
    }

    private void setupRecyclerViews() {
        homeLineupAdapter = new LineupAdapter(new ArrayList<>());
        awayLineupAdapter = new LineupAdapter(new ArrayList<>());
        
        binding.homeLineupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.homeLineupRecyclerView.setAdapter(homeLineupAdapter);
        
        binding.awayLineupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.awayLineupRecyclerView.setAdapter(awayLineupAdapter);
    }

    private void loadLineups() {
        if (matchId == null) return;

        showLoading(true);
        
        ApiClient.getApiService().getMatchLineups(matchId).enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    processLineups(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load lineups", t);
            }
        });
    }

    private void processLineups(List<Player> players) {
        List<Player> homePlayers = new ArrayList<>();
        List<Player> awayPlayers = new ArrayList<>();
        
        for (Player player : players) {
            if (player.getTeamNumber() == 1) {
                homePlayers.add(player);
            } else if (player.getTeamNumber() == 2) {
                awayPlayers.add(player);
            }
        }
        
        homeLineupAdapter.updatePlayers(homePlayers);
        awayLineupAdapter.updatePlayers(awayPlayers);
        
        // Update team names if available
        if (!homePlayers.isEmpty()) {
            binding.homeTeamName.setText("Home Team");
        }
        if (!awayPlayers.isEmpty()) {
            binding.awayTeamName.setText("Away Team");
        }
        
        // Show content
        binding.contentLayout.setVisibility(View.VISIBLE);
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