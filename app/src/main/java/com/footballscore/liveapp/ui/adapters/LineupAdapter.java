package com.footballscore.liveapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.footballscore.liveapp.databinding.ItemPlayerBinding;
import com.footballscore.liveapp.models.Player;
import java.util.List;

public class LineupAdapter extends RecyclerView.Adapter<LineupAdapter.PlayerViewHolder> {
    
    private List<Player> players;

    public LineupAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlayerBinding binding = ItemPlayerBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new PlayerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        holder.bind(players.get(position));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void updatePlayers(List<Player> newPlayers) {
        this.players = newPlayers;
        notifyDataSetChanged();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private ItemPlayerBinding binding;

        public PlayerViewHolder(ItemPlayerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Player player) {
            binding.playerNumber.setText(String.valueOf(player.getShirtNumber()));
            binding.playerName.setText(player.getFullName());
            binding.playerPosition.setText(player.getPositionName());
            
            if (player.getRating() != null && !player.getRating().isEmpty()) {
                binding.playerRating.setText(player.getRating());
            } else {
                binding.playerRating.setText("-");
            }
        }
    }
}