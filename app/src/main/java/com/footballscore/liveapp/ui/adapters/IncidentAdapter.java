package com.footballscore.liveapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ItemIncidentBinding;
import com.footballscore.liveapp.models.Incident;
import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {
    
    private List<Incident> incidents;

    public IncidentAdapter(List<Incident> incidents) {
        this.incidents = incidents;
    }

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIncidentBinding binding = ItemIncidentBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new IncidentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {
        holder.bind(incidents.get(position));
    }

    @Override
    public int getItemCount() {
        return incidents.size();
    }

    public void updateIncidents(List<Incident> newIncidents) {
        this.incidents = newIncidents;
        notifyDataSetChanged();
    }

    static class IncidentViewHolder extends RecyclerView.ViewHolder {
        private ItemIncidentBinding binding;

        public IncidentViewHolder(ItemIncidentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Incident incident) {
            binding.incidentMinute.setText(incident.getMinute() + "'");
            binding.incidentType.setText(incident.getTypeDescription());
            binding.incidentPlayer.setText(incident.getPlayerName());
            
            // Set icon based on incident type
            int iconRes = getIncidentIcon(incident.getType());
            binding.incidentIcon.setImageResource(iconRes);
            
            // Set team indicator
            if (incident.getTeamNumber() == 1) {
                binding.teamIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.home_team_color));
            } else {
                binding.teamIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.away_team_color));
            }
        }
        
        private int getIncidentIcon(int type) {
            switch (type) {
                case Incident.TYPE_GOAL:
                    return R.drawable.ic_goal;
                case Incident.TYPE_YELLOW_CARD:
                    return R.drawable.ic_yellow_card;
                case Incident.TYPE_RED_CARD:
                    return R.drawable.ic_red_card;
                case Incident.TYPE_SUBSTITUTION:
                    return R.drawable.ic_substitution;
                default:
                    return R.drawable.ic_event;
            }
        }
    }
}