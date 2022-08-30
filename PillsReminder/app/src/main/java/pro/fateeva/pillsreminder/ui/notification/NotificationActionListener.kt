package pro.fateeva.pillsreminder.ui.notification

import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.ui.Notificator

interface NotificationActionListener {
    fun onNotificationAction(notificator: Notificator, context: Context, intent: Intent)
}