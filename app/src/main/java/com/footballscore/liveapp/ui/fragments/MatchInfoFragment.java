package com.footballscore.liveapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.footballscore.liveapp.databinding.FragmentMatchInfoBinding;
import com.footballscore.liveapp.models.MatchDetails;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.utils.DateUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchInfoFragment extends Fragment {
    
    private FragmentMatchInfoBinding binding;
    private String matchId;
    private static final String ARG_MATCH_ID = "match_id";
    private static final String TAG = "MatchInfoFragment";

    public static MatchInfoFragment newInstance(String matchId) {
        MatchInfoFragment fragment = new MatchInfoFragment();
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
        binding = FragmentMatchInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMatchInfo();
    }

    private void loadMatchInfo() {
        if (matchId == null) return;

        showLoading(true);
        
        ApiClient.getApiService().getMatchInfo(matchId).enqueue(new Callback<MatchDetails>() {
            @Override
            public void onResponse(Call<MatchDetails> call, Response<MatchDetails> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    displayMatchInfo(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<MatchDetails> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load match info", t);
            }
        });
    }

    private void displayMatchInfo(MatchDetails matchDetails) {
        // Date and Time
        binding.matchDate.setText(DateUtils.formatFullDate(matchDetails.getStartDate()));
        binding.matchTime.setText(DateUtils.formatTime(matchDetails.getStartDate()));
        
        // Venue Information
        if (matchDetails.getVenueName() != null) {
            binding.venueName.setText(matchDetails.getVenueName());
            binding.venueLocation.setText(matchDetails.getVenueCity() + ", " + matchDetails.getVenueCountry());
        } else {
            binding.venueCard.setVisibility(View.GONE);
        }
        
        // Referee Information
        if (matchDetails.getReferees() != null && !matchDetails.getReferees().isEmpty()) {
            MatchDetails.Referee referee = matchDetails.getReferees().get(0);
            binding.refereeName.setText(referee.getName());
            binding.refereeCountry.setText(referee.getCountry());
        } else {
            binding.refereeCard.setVisibility(View.GONE);
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