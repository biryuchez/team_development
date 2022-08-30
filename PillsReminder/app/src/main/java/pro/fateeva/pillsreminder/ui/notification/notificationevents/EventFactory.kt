package pro.fateeva.pillsreminder.ui.notification.notificationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

interface EventFactory {
    fun generateNotificationEvent(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ): NotificationEvent
}