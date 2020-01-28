package com.yangbob.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class TorchAppWidget: AppWidgetProvider()
{
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray)
    {
        Log.i("TEST", "Widget.onUpdate()!")
        for(appWidgetId in appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int)
{
    val widgetText = context.getString(R.string.appwidget_text)
    val views = RemoteViews(context.packageName, R.layout.torch_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    val intent = Intent(context, TorchService::class.java)
    var pendingIntent = PendingIntent.getService(context, 0, intent, 0)
    views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}