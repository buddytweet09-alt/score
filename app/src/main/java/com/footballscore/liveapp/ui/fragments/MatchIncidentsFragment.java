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
import com.footballscore.liveapp.databinding.FragmentMatchIncidentsBinding;
import com.footballscore.liveapp.network.ApiClient;
import com.footballscore.liveapp.ui.adapters.IncidentAdapter;
import com.footballscore.liveapp.models.Incident;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MatchIncidentsFragment extends Fragment {
    
    private FragmentMatchIncidentsBinding binding;
    private String matchId;
    private IncidentAdapter incidentAdapter;
    private static final String ARG_MATCH_ID = "match_id";
    private static final String TAG = "MatchIncidentsFragment";

    public static MatchIncidentsFragment newInstance(String matchId) {
        MatchIncidentsFragment fragment = new MatchIncidentsFragment();
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
        binding = FragmentMatchIncidentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadIncidents();
    }

    private void setupRecyclerView() {
        incidentAdapter = new IncidentAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(incidentAdapter);
    }

    private void loadIncidents() {
        if (matchId == null) return;

        showLoading(true);
        
        ApiClient.getApiService().getMatchIncidents(matchId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    processIncidents(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showLoading(false);
                showError();
                Log.e(TAG, "Failed to load incidents", t);
            }
        });
    }

    private void processIncidents(JsonObject incidentsData) {
        List<Incident> incidents = new ArrayList<>();
        
        try {
            if (incidentsData.has("Incs")) {
                JsonArray incsArray = incidentsData.getAsJsonArray("Incs");
                
                for (int i = 0; i < incsArray.size(); i++) {
                    JsonObject incidentObj = incsArray.get(i).getAsJsonObject();
                    Incident incident = parseIncident(incidentObj);
                    if (incident != null) {
                        incidents.add(incident);
                    }
                }
            }
            
            incidentAdapter.updateIncidents(incidents);
            
            if (incidents.isEmpty()) {
                binding.emptyState.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing incidents", e);
            showError();
        }
    }

    private Incident parseIncident(JsonObject incidentObj) {
        try {
            Incident incident = new Incident();
            
            if (incidentObj.has("Min")) {
                incident.setMinute(incidentObj.get("Min").getAsString());
            }
            
            if (incidentObj.has("IT")) {
                incident.setType(incidentObj.get("IT").getAsInt());
            }
            
            if (incidentObj.has("Pn")) {
                incident.setPlayerName(incidentObj.get("Pn").getAsString());
            }
            
            if (incidentObj.has("Tnb")) {
                incident.setTeamNumber(incidentObj.get("Tnb").getAsInt());
            }
            
            return incident;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing individual incident", e);
            return null;
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}