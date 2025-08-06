package com.footballscore.liveapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.footballscore.liveapp.R;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.ui.activities.MatchDetailsActivity;
import com.google.gson.Gson;

public class NotificationHelper {
    
    private static final String CHANNEL_ID = "match_notifications";
    private static final String CHANNEL_NAME = "Match Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for match events and updates";
    
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    public static void showMatchNotification(Context context, Match match, String title, String message, int notificationId) {
        // Create intent for match details
        Intent intent = new Intent(context, MatchDetailsActivity.class);
        intent.putExtra("match", new Gson().toJson(match));
        intent.putExtra("match_id", match.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 
            notificationId, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        
        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(notificationId, builder.build());
        } catch (SecurityException e) {
            // Handle permission not granted
        }
    }
    
    public static void showGoalNotification(Context context, Match match, String playerName, String minute) {
        String title = "⚽ GOAL!";
        String message = String.format("%s vs %s - %s scored in %s'", 
            match.getHomeTeam().getName(), 
            match.getAwayTeam().getName(), 
            playerName, 
            minute);
        
        showMatchNotification(context, match, title, message, match.getId().hashCode());
    }
    
    public static void showKickoffNotification(Context context, Match match) {
        String title = "⚽ Match Started";
        String message = String.format("%s vs %s - Match has kicked off!", 
            match.getHomeTeam().getName(), 
            match.getAwayTeam().getName());
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 1);
    }
    
    public static void showPreMatchNotification(Context context, Match match, int minutesBefore) {
        String title = "⏰ Match Starting Soon";
        String message = String.format("%s vs %s starts in %d minutes", 
            match.getHomeTeam().getName(), 
            match.getAwayTeam().getName(), 
            minutesBefore);
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 2);
    }
    
    public static void showLineupNotification(Context context, Match match) {
        String title = "📋 Lineups Available";
        String message = String.format("Team lineups are now available for %s vs %s", 
            match.getHomeTeam().getName(), 
            match.getAwayTeam().getName());
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 3);
    }
    
    public static void showCardNotification(Context context, Match match, String playerName, String cardType, String minute) {
        String title = cardType.equals("yellow") ? "🟨 Yellow Card" : "🟥 Red Card";
        String message = String.format("%s vs %s - %s received a %s card in %s'", 
            match.getHomeTeam().getName(), 
            match.getAwayTeam().getName(), 
            playerName, 
            cardType, 
            minute);
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 4);
    }
    
    public static void showHalfTimeNotification(Context context, Match match) {
        String title = "⏸️ Half Time";
        String message = String.format("%s %s - %s %s (Half Time)", 
            match.getHomeTeam().getName(), 
            match.getHomeScore(),
            match.getAwayScore(),
            match.getAwayTeam().getName());
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 5);
    }
    
    public static void showFullTimeNotification(Context context, Match match) {
        String title = "🏁 Full Time";
        String message = String.format("%s %s - %s %s (Final)", 
            match.getHomeTeam().getName(), 
            match.getHomeScore(),
            match.getAwayScore(),
            match.getAwayTeam().getName());
        
        showMatchNotification(context, match, title, message, match.getId().hashCode() + 6);
    }
}