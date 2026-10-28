package com.holyeyed.volumecontrol

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.RemoteViews

class VolumeWidget : AppWidgetProvider() {

    companion object {
        private const val ACTION_INCREASE = "ACTION_INCREASE_VOLUME"
        private const val ACTION_DECREASE = "ACTION_DECREASE_VOLUME"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        when (intent.action) {
            ACTION_INCREASE -> {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_SHOW_UI
                )
            }
            ACTION_DECREASE -> {
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_SHOW_UI
                )
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Sự kiện bấm nút Tăng trên widget
        val intentIncrease = Intent(context, VolumeWidget::class.java).apply {
            action = ACTION_INCREASE
        }
        val pendingIncrease = PendingIntent.getBroadcast(
            context, 0, intentIncrease, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btnWidgetIncrease, pendingIncrease)

        // Sự kiện bấm nút Giảm trên widget
        val intentDecrease = Intent(context, VolumeWidget::class.java).apply {
            action = ACTION_DECREASE
        }
        val pendingDecrease = PendingIntent.getBroadcast(
            context, 1, intentDecrease, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btnWidgetDecrease, pendingDecrease)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}