package com.footballscore.liveapp.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.footballscore.liveapp.databinding.FragmentFavoritesBinding;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.ui.adapters.MatchAdapter;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class FavoritesFragment extends Fragment {
    
    private FragmentFavoritesBinding binding;
    private MatchAdapter matchAdapter;
    private List<Match> favoriteMatches = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        sharedPreferences = requireContext().getSharedPreferences("app_preferences", MODE_PRIVATE);
        
        setupRecyclerView();
        loadFavorites();
    }

    private void setupRecyclerView() {
        matchAdapter = new MatchAdapter(favoriteMatches, this::onMatchClick, this::onFavoriteClick);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(matchAdapter);
    }

    private void loadFavorites() {
        Set<String> favoriteIds = sharedPreferences.getStringSet("favorite_matches", new HashSet<>());
        Set<String> favoriteMatchesJson = sharedPreferences.getStringSet("favorite_matches_data", new HashSet<>());
        
        favoriteMatches.clear();
        
        for (String matchJson : favoriteMatchesJson) {
            try {
                Match match = new Gson().fromJson(matchJson, Match.class);
                if (favoriteIds.contains(match.getId())) {
                    match.setFavorite(true);
                    favoriteMatches.add(match);
                }
            } catch (Exception e) {
                // Skip invalid JSON
            }
        }
        
        matchAdapter.notifyDataSetChanged();
        
        // Show empty state if no favorites
        binding.emptyState.setVisibility(favoriteMatches.isEmpty() ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(favoriteMatches.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void onMatchClick(Match match) {
        // Navigate to match details
        android.content.Intent intent = new android.content.Intent(getContext(), 
            com.footballscore.liveapp.ui.activities.MatchDetailsActivity.class);
        intent.putExtra("match", new Gson().toJson(match));
        intent.putExtra("match_id", match.getId());
        startActivity(intent);
    }

    private void onFavoriteClick(Match match) {
        // Remove from favorites
        match.setFavorite(false);
        removeFavorite(match);
        favoriteMatches.remove(match);
        matchAdapter.notifyDataSetChanged();
        
        // Update empty state
        binding.emptyState.setVisibility(favoriteMatches.isEmpty() ? View.VISIBLE : View.GONE);
        binding.recyclerView.setVisibility(favoriteMatches.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void removeFavorite(Match match) {
        Set<String> favoriteIds = new HashSet<>(sharedPreferences.getStringSet("favorite_matches", new HashSet<>()));
        Set<String> favoriteMatchesData = new HashSet<>(sharedPreferences.getStringSet("favorite_matches_data", new HashSet<>()));
        
        favoriteIds.remove(match.getId());
        favoriteMatchesData.remove(new Gson().toJson(match));
        
        sharedPreferences.edit()
                .putStringSet("favorite_matches", favoriteIds)
                .putStringSet("favorite_matches_data", favoriteMatchesData)
                .apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites(); // Refresh favorites when returning to fragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}