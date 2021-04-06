package com.example.githubuser.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.githubuser.R
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.util.ALARM_REQUEST_CODE
import com.example.githubuser.util.DEFAULT_REQUEST_CODE
import com.example.githubuser.util.EXTRA_ALARM_MESSAGE
import com.example.githubuser.util.EXTRA_ALARM_TITLE
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_ALARM_TITLE)
        val message = intent.getStringExtra(EXTRA_ALARM_MESSAGE)
        showAlarmNotification(
            context,
            title ?: context.getString(R.string.default_notification_title),
            message ?: context.getString(R.string.default_notification_message)
        )
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
    ) {
        val channelId = "Channel_Alarm"
        val channelName = "Alarm Channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent =
            PendingIntent.getActivity(context, DEFAULT_REQUEST_CODE, notificationIntent, 0)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_github)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ALARM_REQUEST_CODE, notification)
    }

    companion object {
        fun setAlarm(
            context: Context,
            title: String,
            message: String,
            time: Calendar
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(EXTRA_ALARM_TITLE, title)
                putExtra(EXTRA_ALARM_MESSAGE, message)
            }
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                time.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        fun cancelAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, 0)
            pendingIntent.cancel()
            alarmManager.cancel(pendingIntent)
        }

        fun isAlarmSet(context: Context): Boolean {
            val intent = Intent(context, AlarmReceiver::class.java)

            return PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_NO_CREATE
            ) != null
        }
    }
}