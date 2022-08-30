package pro.fateeva.pillsreminder.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Ресивер интентов от AlarmManager.
 * Когда AlarmManager сообщает, что настало время события "Прием лекарства", ресивер
 * просит medicationNotifier показать уведомление с параметрами из пришедшего интента
 */
class MedicationEventReceiver : BroadcastReceiver() {

    private val medicationNotifier: MedicationNotification = MedicationNotifier()

    override fun onReceive(context: Context, intent: Intent) {
        medicationNotifier.showNotification(context, intent)
    }
}