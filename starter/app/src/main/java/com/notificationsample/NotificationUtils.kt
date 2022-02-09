package com.notificationsample

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder

private const val NOTIFICATION_ID = 0

/**
 * Builds and displays the notification that displays the provided [messageBody] and allows the user
 * to view the details of the download provided in the [arguments].
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
    arguments: Bundle,
    messageBody: String,
    applicationContext: Context
) {
    // Used to display the details of the download in the app using the deep linking functionality
    // of the navigation library https://developer.android.com/guide/navigation/navigation-deep-link#explicit
    // An example of doing this can be found at
    // https://stackoverflow.com/questions/26608627/how-to-open-fragment-page-when-pressed-a-notification-in-android
    val pendingIntent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.main_nav_graph)
        .setDestination(R.id.downloadDetailsFragment)
        .setArguments(arguments)
        .createPendingIntent()

    // Use the inbox style notification with the provided file information in the title. More
    // details about this style are at
    // https://developer.android.com/reference/kotlin/androidx/core/app/NotificationCompat.InboxStyle
    val messageStyle =
        NotificationCompat.InboxStyle()
            .setBigContentTitle(applicationContext.getString(R.string.notification_title))
            .addLine(messageBody)

    // Build the notification using the passed in title and add a button that will open the
    // details of the download in the app
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    ).setContentTitle(applicationContext.getString(R.string.notification_title))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setAutoCancel(true)
        .setStyle(messageStyle)
        .addAction(
            R.drawable.ic_baseline_cloud_download_24,
            applicationContext.getString(R.string.download_notification_channel_Action),
            pendingIntent
        ).setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Display the notification
    notify(NOTIFICATION_ID, builder.build())
}

