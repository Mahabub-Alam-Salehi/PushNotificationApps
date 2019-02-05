package com.mahabub.pushnotificationapps;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.app.PendingIntent;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body) {


        Intent intent = new Intent( context, ProfileActivity.class );
        //   Intent intent  = new Intent( context, ProfileActivity.class );
        //  PendingIntent pendingIntent = PendingIn

        PendingIntent pendingIntent = PendingIntent.getActivities( context, 100, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder( context, MainActivity.CHANNEL_ID )
                        .setSmallIcon( R.drawable.pepsi_logo )
                        .setContentTitle( title )
                        .setContentIntent( pendingIntent )
                        .setAutoCancel( true )
                        .setContentText( body )
                        .setPriority( NotificationCompat.PRIORITY_DEFAULT );


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from( context );
        notificationManager.notify( 1, mBuilder.build() );

    }

}


