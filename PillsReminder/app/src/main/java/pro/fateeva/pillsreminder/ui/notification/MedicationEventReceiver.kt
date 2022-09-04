package pro.fateeva.pillsreminder.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import pro.fateeva.pillsreminder.clean.MedicationInteractor
import pro.fateeva.pillsreminder.clean.MedicationReminderRepository
import pro.fateeva.pillsreminder.ui.notification.notificationcreator.MedicationNotification
import pro.fateeva.pillsreminder.ui.notification.notificationcreator.MedicationNotifier

/**
 * Ресивер интентов от AlarmManager.
 * Когда AlarmManager сообщает, что настало время события "Прием лекарства", ресивер
 * просит medicationNotifier показать уведомление с параметрами из пришедшего интента
 */
class MedicationEventReceiver : BroadcastReceiver(), KoinComponent {
    private val medicationInteractor: MedicationInteractor by inject()
    private val medicationReminderRepository : MedicationReminderRepository by inject()

    private val medicationNotifier: MedicationNotification = MedicationNotifier()

    override fun onReceive(context: Context, intent: Intent) {
        medicationNotifier.showNotification(context, intent)

        val id = intent.extras?.getInt(MedicationNotifier.NOTIFICATION_ID_EXTRA_KEY) ?: error("ID can not be null")
        val reminderTime = intent.extras?.getLong(MedicationNotifier.REMINDER_TIME_EXTRA_KEY) ?: error("RemiderTime can not be null")
        medicationInteractor.onNotificationShown(medicationReminderRepository.getMedicationReminder(id), reminderTime)
    }
}