package pro.fateeva.pillsreminder.ui.notification

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.ui.Notificator

class MedicationActionListener : NotificationActionListener {

    companion object {
        const val MEDICATION_EVENT_INTENT_CATEGORY = "MEDICATION_EVENT_INTENT_CATEGORY"
        const val GET_DRUG_ACTION_EXTRA_KEY = "GET_DRUG_ACTION"
        const val CANCEL_DRUG_ACTION_EXTRA_KEY = "CANCEL_DRUG_ACTION_EXTRA_KEY"
        const val NOTIFICATION_ID_EXTRA_KEY = "NOTIFICATION_ID"
    }

    override fun onNotificationAction(notificator: Notificator, context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        intent.categories?.let {
            if (intent.categories.contains(MEDICATION_EVENT_INTENT_CATEGORY)) {
                val extras = intent.extras
                if (extras != null) {
                    if (extras.containsKey(GET_DRUG_ACTION_EXTRA_KEY)) {
                        notificator.onGetDrugAction(
                            extras.getString(GET_DRUG_ACTION_EXTRA_KEY).toString())
                    } else if (extras.containsKey(CANCEL_DRUG_ACTION_EXTRA_KEY)) {
                        notificator.onCancelDrugAction(
                            extras.getString(CANCEL_DRUG_ACTION_EXTRA_KEY).toString())
                    }
                    notificationManager.cancel(extras.getInt(NOTIFICATION_ID_EXTRA_KEY))
                }
            }
        }
    }
}