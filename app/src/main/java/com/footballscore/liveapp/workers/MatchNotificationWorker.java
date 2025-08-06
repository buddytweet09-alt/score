package com.footballscore.liveapp.workers;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.utils.NotificationHelper;
import com.google.gson.Gson;

public class MatchNotificationWorker extends Worker {
    
    public MatchNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    
    @NonNull
    @Override
    public Result doWork() {
        // Check if notifications are enabled
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        
        if (!notificationsEnabled) {
            return Result.success();
        }
        
        try {
            String matchJson = getInputData().getString("match_json");
            String notificationType = getInputData().getString("notification_type");
            
            if (matchJson == null || notificationType == null) {
                return Result.failure();
            }
            
            Match match = new Gson().fromJson(matchJson, Match.class);
            
            switch (notificationType) {
                case "pre_match":
                    int minutesBefore = getInputData().getInt("minutes_before", 15);
                    NotificationHelper.showPreMatchNotification(getApplicationContext(), match, minutesBefore);
                    break;
                    
                case "kickoff":
                    NotificationHelper.showKickoffNotification(getApplicationContext(), match);
                    break;
                    
                case "lineup":
                    NotificationHelper.showLineupNotification(getApplicationContext(), match);
                    break;
                    
                default:
                    return Result.failure();
            }
            
            return Result.success();
            
        } catch (Exception e) {
            return Result.failure();
        }
    }
}