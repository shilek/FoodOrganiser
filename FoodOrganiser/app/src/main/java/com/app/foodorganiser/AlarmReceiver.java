package com.app.foodorganiser;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                    .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                    .setContentTitle(intent.getExtras().getString("title"))
                    .setContentText(intent.getExtras().getString("text"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(intent.getExtras().getInt("id"), builder.build());
    }
}
