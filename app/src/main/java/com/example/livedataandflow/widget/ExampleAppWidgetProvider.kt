package com.example.livedataandflow.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.example.livedataandflow.MainActivity
import com.example.livedataandflow.R

/**
 * Implementation of App Widget functionality.
 */
class ExampleAppWidgetProvider : AppWidgetProvider() {
    private val TAG = javaClass.simpleName

    companion object{
        const val ACTION_UPDATE_COUNT = "android.action.APPWIDGET_UPDATE_COUNT"
        const val EXTRA_COUNT = "EXTRA_COUNT"
    }

    // 위젯 메타 데이터를 구성 할 때 updatePeriodMillis 라는 업데이트 주기 값을 설정하게 되며, 이 주기에 따라 호출 됩니다.
    // 또한 앱 위젯이 추가 될 때도 호출 되므로 Service 와의 상호작용 등의 초기 설정이 필요 할 경우에도 이 메소드를 통해 구현합니다.
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
            .let { intent ->
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.example_app_widget_provider
            ).apply {
//                setOnClickPendingIntent(R.id.appwidget_button_1,
//                    Intent(context, MainActivity::class.java)
//                        .apply {
//                            action = "thanks"
//                            putExtra("data", 0)
//                            addCategory(Intent.ACTION_VIEW)
//                        }.let {
//                            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//                        }
//                )
                setOnClickPendingIntent(R.id.appwidget_button_1, pendingIntent)
                setOnClickPendingIntent(
                    R.id.appwidget_button_2,
                    Intent(context, WidgetActivity::class.java)
                        .apply {
                            action = "save"
                            putExtra("data", 1)
                        }.let {
                            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                        }
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    // 앱 위젯은 여러개가 등록 될 수 있는데, 최초의 앱 위젯이 등록 될 때 호출 됩니다. (각 앱 위젯 인스턴스가 등록 될때마다 호출 되는 것이 아님)
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context)
    }

    // onEnabled() 와는 반대로 마지막의 최종 앱 위젯 인스턴스가 삭제 될 때 호출 됩니다
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context)
    }

    // android 4.1 에 추가 된 메소드 이며, 앱 위젯이 등록 될 때와 앱 위젯의 크기가 변경 될 때 호출 됩니다.
    // 이때, Bundle 에 위젯 너비/높이의 상한값/하한값 정보를 넘겨주며 이를 통해 컨텐츠를 표시하거나 숨기는 등의 동작을 구현 합니다
    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d(TAG, "onAppWidgetOptionsChanged ${newOptions.getString("width")}")
    }

    // 이 메소드는 앱 데이터가 구글 시스템에 백업 된 이후 복원 될 때 만약 위젯 데이터가 있다면 데이터가 복구 된 이후 호출 됩니다.
    // 일반적으로 사용 될 경우는 흔치 않습니다.
    // 위젯 ID 는 UID 별로 관리 되는데 이때 복원 시점에서 ID 가 변경 될 수 있으므로 백업 시점의 oldID 와 복원 후의 newID 를 전달합니다
    override fun onRestored(
        context: Context,
        oldWidgetIds: IntArray,
        newWidgetIds: IntArray
    ) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    // 해당 앱 위젯이 삭제 될 때 호출 됩니다
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }

    // 앱의 브로드캐스트를 수신하며 해당 메서드를 통해 각 브로드캐스트에 맞게 메서드를 호출한다.
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action){
            ACTION_UPDATE_COUNT ->{
                Log.d(TAG, "ACTION_UPDATE_COUNT")
                Log.d(TAG, "intent.extras?.getInt(EXTRA_COUNT): ${intent.extras?.getInt(EXTRA_COUNT)?.toString()}")
                intent.extras?.getInt(EXTRA_COUNT)?.let { onUpdateCount(context, it) }
            }
        }
    }

    private fun onUpdateCount(context: Context, newCount: Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.example_app_widget_provider).apply {
            setTextViewText(R.id.appwidget_button_1, (newCount - 1).toString())
            setTextViewText(R.id.appwidget_text, newCount.toString())
            setTextViewText(R.id.appwidget_button_2, (newCount + 1).toString())
        }

        AppWidgetManager.getInstance(context).apply {
            val appWidgetIds = getAppWidgetIds(ComponentName(context, ExampleAppWidgetProvider::class.java))
            updateAppWidget(appWidgetIds, remoteViews)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.example_app_widget_provider)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}