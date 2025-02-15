package com.android.template.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.android.template.R
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.ui.navigation.NavigationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.leolin.shortcutbadger.ShortcutBadger
import javax.inject.Inject

class NotificationService : FirebaseMessagingService() {

    private var notificationId = 1

    @Inject
    lateinit var preferences: PreferencesHelper
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferences.setFCMToken(token)
        Log.i(TAG, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notifyPush(remoteMessage.toIntent())
    }

    // This used for handle and parse push notifications when application is closed
    override fun handleIntent(handleIntent: Intent) {
        notifyPush(handleIntent)
    }

    private fun notifyPush(handleIntent: Intent) {
        val intent = NavigationActivity.newIntent(applicationContext, handleIntent.extras)
        val requestID = 1
        var flag = PendingIntent.FLAG_ONE_SHOT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            flag = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        }
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, requestID, intent, flag)

        val channelId =
            "${getString(R.string.app_name)} ${getString(R.string.notifications_admin_channel_description)}"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val badgeCount = handleIntent.extras?.getString(EXTRA_BADGE_COUNT, "0")?.let {
            it.toCharArray().filter { char -> char.isDigit() }
        }?.joinToString("")?.toInt()
        val pushTitle = handleIntent.extras?.getString(EXTRA_TITLE, "")
        val pushContentText = handleIntent.extras?.getString(EXTRA_BODY, "")?.let {
            HtmlCompat.fromHtml(
                it,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(pushTitle)
            .setContentText(pushContentText)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationBuilder.setSmallIcon(R.drawable.ic_push_notification)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(notificationId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name =
                "${getString(R.string.app_name)} ${getString(R.string.notifications_admin_channel_name)}"
            val channel =
                NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)

            channel.setShowBadge(channel.canShowBadge())
            notificationManager.createNotificationChannel(channel)
        }

        if (!pushTitle.isNullOrBlank() || !pushContentText.isNullOrBlank()) {
            val notification = notificationBuilder.build()
            badgeCount?.let {
                ShortcutBadger.applyNotification(applicationContext, notification, it)
            }
            notificationManager.notify(notificationId, notification)
        }
    }


    companion object {
        private const val TAG = "onNewFCMToken"

        const val EXTRA_TITLE = "gcm.notification.title"
        const val EXTRA_BODY = "gcm.notification.body"
        const val EXTRA_BADGE_COUNT = "gcm.notification.notification_count"
    }
}