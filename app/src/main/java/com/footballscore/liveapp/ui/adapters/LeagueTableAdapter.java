package com.footballscore.liveapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ItemLeagueTableBinding;
import com.footballscore.liveapp.models.LeagueTable;
import java.util.List;

public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableAdapter.TableViewHolder> {
    
    private List<LeagueTable> table;

    public LeagueTableAdapter(List<LeagueTable> table) {
        this.table = table;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeagueTableBinding binding = ItemLeagueTableBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new TableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        holder.bind(table.get(position));
    }

    @Override
    public int getItemCount() {
        return table.size();
    }

    public void updateTable(List<LeagueTable> newTable) {
        this.table = newTable;
        notifyDataSetChanged();
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        private ItemLeagueTableBinding binding;

        public TableViewHolder(ItemLeagueTableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LeagueTable team) {
            binding.position.setText(String.valueOf(team.getPosition()));
            binding.teamName.setText(team.getTeamName());
            binding.played.setText(String.valueOf(team.getPlayed()));
            binding.wins.setText(String.valueOf(team.getWins()));
            binding.draws.setText(String.valueOf(team.getDraws()));
            binding.losses.setText(String.valueOf(team.getLosses()));
            binding.goalDifference.setText(String.valueOf(team.getGoalDifference()));
            binding.points.setText(String.valueOf(team.getPoints()));

            // Load team logo
            Glide.with(binding.getRoot().getContext())
                    .load(team.getTeamLogo())
                    .placeholder(R.drawable.ic_team_placeholder)
                    .error(R.drawable.ic_team_placeholder)
                    .into(binding.teamLogo);

            // Set position background color based on position
            int position = team.getPosition();
            if (position <= 4) {
                // Champions League
                binding.positionIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.champions_league_color));
            } else if (position <= 6) {
                // Europa League
                binding.positionIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.europa_league_color));
            } else if (position >= 18) {
                // Relegation
                binding.positionIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.relegation_color));
            } else {
                // Normal
                binding.positionIndicator.setBackgroundColor(
                    binding.getRoot().getContext().getColor(R.color.divider_color));
            }
        }
    }
}