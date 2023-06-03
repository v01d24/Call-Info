package com.v01d24.callinfo;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.v01d24.callinfo.registry.PhoneNumberInfo;

public class Notificator {
    private static final String TAG = Notificator.class.getName();

    private static final int INCOMING_CALL_REQUEST_CODE = 1;

    private final Context context;

    public Notificator(@NonNull Context context) {
        this.context = context;
    }

    public void createIncomingCallNotification(@NonNull String phoneNumber,
                                               @NonNull PhoneNumberInfo info) {
        Intent webPageIntent = new Intent(Intent.ACTION_VIEW, info.webPageUri);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, INCOMING_CALL_REQUEST_CODE, webPageIntent, 0
        );
        int icon;
        switch (info.score) {
            case NEGATIVE:
                icon = R.drawable.ic_call_negative;
                break;
            case POSITIVE:
                icon = R.drawable.ic_call_positive;
                break;
            default:
                icon = R.drawable.ic_call_neutral;
                break;
        }
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(phoneNumber)
                .setContentText(info.summary)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
