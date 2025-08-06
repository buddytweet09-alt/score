package com.footballscore.liveapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.databinding.ItemMatchBinding;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.utils.DateUtils;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    
    private List<Match> matches;
    private OnMatchClickListener onMatchClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;

    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Match match);
    }

    public MatchAdapter(List<Match> matches, OnMatchClickListener onMatchClickListener, 
                       OnFavoriteClickListener onFavoriteClickListener) {
        this.matches = matches;
        this.onMatchClickListener = onMatchClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMatchBinding binding = ItemMatchBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new MatchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.bind(matches.get(position));
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void updateMatches(List<Match> newMatches) {
        this.matches = newMatches;
        notifyDataSetChanged();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        private ItemMatchBinding binding;

        public MatchViewHolder(ItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Match match) {
            // Set team names
            binding.homeTeamName.setText(match.getHomeTeam().getName());
            binding.awayTeamName.setText(match.getAwayTeam().getName());

            // Set scores
            binding.homeScore.setText(match.getHomeScore());
            binding.awayScore.setText(match.getAwayScore());

            // Load team logos
            Glide.with(binding.getRoot().getContext())
                    .load(match.getHomeTeam().getLogo())
                    .placeholder(R.drawable.ic_team_placeholder)
                    .error(R.drawable.ic_team_placeholder)
                    .into(binding.homeTeamLogo);

            Glide.with(binding.getRoot().getContext())
                    .load(match.getAwayTeam().getLogo())
                    .placeholder(R.drawable.ic_team_placeholder)
                    .error(R.drawable.ic_team_placeholder)
                    .into(binding.awayTeamLogo);

            // Set match status and styling
            if (match.isLive()) {
                binding.matchStatus.setText(match.getMinute() + "' LIVE");
                binding.matchStatus.setTextColor(binding.getRoot().getContext().getColor(R.color.live_green));
                binding.liveIndicator.setVisibility(View.VISIBLE);
            } else if (match.isFinished()) {
                binding.matchStatus.setText("FT");
                binding.matchStatus.setTextColor(binding.getRoot().getContext().getColor(R.color.finished_gray));
                binding.liveIndicator.setVisibility(View.GONE);
            } else {
                binding.matchStatus.setText(DateUtils.formatMatchTime(match.getStartTime()));
                binding.matchStatus.setTextColor(binding.getRoot().getContext().getColor(R.color.upcoming_blue));
                binding.liveIndicator.setVisibility(View.GONE);
            }

            // Set competition info
            if (match.getCompetition() != null) {
                binding.competitionName.setText(match.getCompetition().getName());
                Glide.with(binding.getRoot().getContext())
                        .load(match.getCompetition().getLogo())
                        .placeholder(R.drawable.ic_competition_placeholder)
                        .error(R.drawable.ic_competition_placeholder)
                        .into(binding.competitionLogo);
            }

            // Set favorite status
            binding.favoriteButton.setImageResource(match.isFavorite() ? 
                R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);

            // Click listeners
            binding.getRoot().setOnClickListener(v -> {
                if (onMatchClickListener != null) {
                    onMatchClickListener.onMatchClick(match);
                }
            });

            binding.favoriteButton.setOnClickListener(v -> {
                if (onFavoriteClickListener != null) {
                    onFavoriteClickListener.onFavoriteClick(match);
                }
            });

            // Show favorite indicator if match is favorite
            binding.favoriteIndicator.setVisibility(match.isFavorite() ? View.VISIBLE : View.GONE);
        }
    }
}