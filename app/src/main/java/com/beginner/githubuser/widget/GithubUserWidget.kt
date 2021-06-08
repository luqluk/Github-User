package com.beginner.githubuser.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.beginner.githubuser.R
import com.shashank.sony.fancytoastlib.FancyToast


class GithubUserWidget : AppWidgetProvider() {
    companion object {
        private const val TOAST_ACTION = "com.beginner.githubuser.TOAST"
        private const val UPDATE_WIDGET = "com.beginner.githubuser.UPDATE_WIDGET"
        const val EXTRA_ITEM = "com.beginner.EXTRA_ITEM"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, WidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.github_user_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty)

            val toastIntent = Intent(context, GithubUserWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPending =
                PendingIntent.getBroadcast(
                    context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
            views.setPendingIntentTemplate(R.id.stack_view, toastPending)

            appWidgetManager.updateAppWidget(appWidgetId, views)

        }


    }

    fun refreshWidget(context: Context) {
        val i = Intent(context, GithubUserWidget::class.java)
        i.action = UPDATE_WIDGET
        context.sendBroadcast(i)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val message = context.getString(R.string.widget_on_message)
        FancyToast.makeText(context, message, Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
    }

    override fun onDisabled(context: Context) {
        val message = context.getString(R.string.widget_off_message)
        FancyToast.makeText(context, message, Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val login = intent?.getStringExtra(EXTRA_ITEM)

        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                FancyToast.makeText(context, login, Toast.LENGTH_SHORT, FancyToast.SUCCESS, false)
                    .show()
            }

            if (intent.action == UPDATE_WIDGET) {
                val manager = AppWidgetManager.getInstance(context)
                val ids = manager.getAppWidgetIds(ComponentName(context!!, GithubUserWidget::class.java))
                manager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
            }
        }
    }
}

