package com.footballscore.liveapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ItemLeagueBinding;
import com.footballscore.liveapp.models.League;
import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder> {
    
    private List<League> leagues;
    private OnLeagueClickListener onLeagueClickListener;

    public interface OnLeagueClickListener {
        void onLeagueClick(League league);
    }

    public LeagueAdapter(List<League> leagues, OnLeagueClickListener onLeagueClickListener) {
        this.leagues = leagues;
        this.onLeagueClickListener = onLeagueClickListener;
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeagueBinding binding = ItemLeagueBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new LeagueViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        holder.bind(leagues.get(position));
    }

    @Override
    public int getItemCount() {
        return leagues.size();
    }

    public void updateLeagues(List<League> newLeagues) {
        this.leagues = newLeagues;
        notifyDataSetChanged();
    }

    class LeagueViewHolder extends RecyclerView.ViewHolder {
        private ItemLeagueBinding binding;

        public LeagueViewHolder(ItemLeagueBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(League league) {
            binding.leagueName.setText(league.getName());
            binding.leagueCountry.setText(league.getCountry());

            // Load league logo
            Glide.with(binding.getRoot().getContext())
                    .load(league.getLogo())
                    .placeholder(R.drawable.ic_competition_placeholder)
                    .error(R.drawable.ic_competition_placeholder)
                    .into(binding.leagueLogo);

            // Click listener
            binding.getRoot().setOnClickListener(v -> {
                if (onLeagueClickListener != null) {
                    onLeagueClickListener.onLeagueClick(league);
                }
            });
        }
    }
}