package com.footballscore.liveapp.utils;

import android.content.Context;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.workers.MatchNotificationWorker;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {
    
    public static void schedulePreMatchNotification(Context context, Match match, int minutesBefore) {
        long currentTime = System.currentTimeMillis();
        long matchTime = match.getStartTime();
        long notificationTime = matchTime - (minutesBefore * 60 * 1000);
        long delay = notificationTime - currentTime;
        
        if (delay > 0) {
            Data inputData = new Data.Builder()
                    .putString("match_json", new Gson().toJson(match))
                    .putString("notification_type", "pre_match")
                    .putInt("minutes_before", minutesBefore)
                    .build();
            
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MatchNotificationWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag("pre_match_" + match.getId())
                    .build();
            
            WorkManager.getInstance(context).enqueue(workRequest);
        }
    }
    
    public static void scheduleKickoffNotification(Context context, Match match) {
        long currentTime = System.currentTimeMillis();
        long matchTime = match.getStartTime();
        long delay = matchTime - currentTime;
        
        if (delay > 0) {
            Data inputData = new Data.Builder()
                    .putString("match_json", new Gson().toJson(match))
                    .putString("notification_type", "kickoff")
                    .build();
            
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MatchNotificationWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag("kickoff_" + match.getId())
                    .build();
            
            WorkManager.getInstance(context).enqueue(workRequest);
        }
    }
    
    public static void cancelMatchNotifications(Context context, String matchId) {
        WorkManager.getInstance(context).cancelAllWorkByTag("pre_match_" + matchId);
        WorkManager.getInstance(context).cancelAllWorkByTag("kickoff_" + matchId);
    }
}