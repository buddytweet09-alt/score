package com.footballscore.liveapp.ui.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.FragmentHomeBinding;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.models.MatchResponse;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.adapters.MatchAdapter;
import com.footballscore.liveapp.utils.DateUtils;
import com.footballscore.liveapp.utils.NotificationScheduler;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentHomeBinding binding;
    private MatchAdapter matchAdapter;
    private List<Match> allMatches = new ArrayList<>();
    private List<Match> favoriteMatches = new ArrayList<>();
    private String selectedDate;
    private String currentFilter = "LIVE";
    private SharedPreferences sharedPreferences;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireContext().getSharedPreferences("app_preferences", MODE_PRIVATE);
        selectedDate = DateUtils.getCurrentDate();

        setupRecyclerView();
        setupSwipeRefresh();
        setupTabs();
        setupDatePicker();
        loadFavorites();
        loadMatches();
    }

    private void setupRecyclerView() {
        matchAdapter = new MatchAdapter(new ArrayList<>(), this::onMatchClick, this::onFavoriteClick);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(matchAdapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this);
        binding.swipeRefresh.setColorSchemeResources(
                R.color.primary_color,
                R.color.secondary_color
        );
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("LIVE"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("UPCOMING"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FINISHED"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentFilter = tab.getText().toString();
                filterMatches();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupDatePicker() {
        binding.dateSelector.setText(DateUtils.formatDateForDisplay(selectedDate));
        binding.dateSelector.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    if (binding != null) {
                        binding.dateSelector.setText(DateUtils.formatDateForDisplay(selectedDate));
                    }
                    loadMatches();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadMatches() {
        showLoading(true);

        Call<MatchResponse> call;
        if (currentFilter.equals("LIVE")) {
            call = ApiClient.getApiService().getLiveMatches("soccer");
        } else {
            call = ApiClient.getApiService().getMatchesByDate("soccer", selectedDate);
        }

        call.enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                // Check if fragment is still attached and binding is not null
                if (!isAdded() || binding == null) {
                    return;
                }

                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    processMatchResponse(response.body());
                } else {
                    showError("Failed to load matches");
                }
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                // Check if fragment is still attached and binding is not null
                if (!isAdded() || binding == null) {
                    return;
                }

                showLoading(false);
                showError("Network error: " + t.getMessage());
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    private void processMatchResponse(MatchResponse response) {
        if (binding == null) {
            return;
        }

        allMatches.clear();

        if (response.getStages() != null) {
            for (MatchResponse.Stage stage : response.getStages()) {
                if (stage.getMatches() != null) {
                    for (Match match : stage.getMatches()) {
                        // Set favorite status
                        match.setFavorite(isFavorite(match.getId()));
                        allMatches.add(match);
                        
                        // Schedule notifications for favorite matches
                        if (match.isFavorite() && match.isUpcoming()) {
                            scheduleMatchNotifications(match);
                        }
                    }
                }
            }
        }

        filterMatches();
    }

    private void scheduleMatchNotifications(Match match) {
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        if (!notificationsEnabled || getContext() == null) {
            return;
        }
        
        // Schedule 15 minutes before kickoff
        NotificationScheduler.schedulePreMatchNotification(getContext(), match, 15);
        
        // Schedule kickoff notification
        NotificationScheduler.scheduleKickoffNotification(getContext(), match);
    }
    private void filterMatches() {
        if (binding == null || matchAdapter == null) {
            return;
        }

        List<Match> filteredMatches = new ArrayList<>();

        // Add favorites first if they exist
        for (Match match : allMatches) {
            if (match.isFavorite()) {
                if (matchesFilter(match)) {
                    filteredMatches.add(match);
                }
            }
        }

        // Add non-favorite matches
        for (Match match : allMatches) {
            if (!match.isFavorite() && matchesFilter(match)) {
                filteredMatches.add(match);
            }
        }

        matchAdapter.updateMatches(filteredMatches);

        // Show/hide favorites section
        binding.favoritesSection.setVisibility(
                filteredMatches.stream().anyMatch(Match::isFavorite) ? View.VISIBLE : View.GONE
        );

        // Show empty state if no matches
        binding.emptyState.setVisibility(filteredMatches.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean matchesFilter(Match match) {
        switch (currentFilter) {
            case "LIVE":
                return match.isLive();
            case "UPCOMING":
                return match.isUpcoming();
            case "FINISHED":
                return match.isFinished();
            default:
                return true;
        }
    }

    private void onMatchClick(Match match) {
        // Check if fragment is still attached
        if (!isAdded() || getContext() == null) {
            return;
        }

        // Navigate to match details
        android.content.Intent intent = new android.content.Intent(getContext(),
                com.footballscore.liveapp.ui.activities.MatchDetailsActivity.class);
        intent.putExtra("match", new Gson().toJson(match));
        intent.putExtra("match_id", match.getId());
        startActivity(intent);
    }

    private void onFavoriteClick(Match match) {
        if (matchAdapter == null) {
            return;
        }

        match.setFavorite(!match.isFavorite());
        saveFavorite(match.getId(), match.isFavorite());
        
        // Schedule or cancel notifications based on favorite status
        if (match.isFavorite() && match.isUpcoming()) {
            scheduleMatchNotifications(match);
        } else if (!match.isFavorite() && getContext() != null) {
            NotificationScheduler.cancelMatchNotifications(getContext(), match.getId());
        }
        
        matchAdapter.notifyDataSetChanged();
        filterMatches(); // Refresh to update favorites section
    }

    private void loadFavorites() {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_matches", new HashSet<>());
        favoriteMatches.clear();
        // Favorites will be loaded when matches are fetched
    }

    private void saveFavorite(String matchId, boolean isFavorite) {
        Set<String> favorites = new HashSet<>(sharedPreferences.getStringSet("favorite_matches", new HashSet<>()));
        if (isFavorite) {
            favorites.add(matchId);
        } else {
            favorites.remove(matchId);
        }
        sharedPreferences.edit().putStringSet("favorite_matches", favorites).apply();
    }

    private boolean isFavorite(String matchId) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_matches", new HashSet<>());
        return favorites.contains(matchId);
    }

    private void showLoading(boolean show) {
        // Add null check for binding
        if (binding == null) {
            return;
        }

        binding.swipeRefresh.setRefreshing(show);
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        // Check if fragment is still attached before showing toast
        if (!isAdded() || getContext() == null) {
            return;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        loadMatches();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}