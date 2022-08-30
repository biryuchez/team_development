package pro.fateeva.pillsreminder.ui.notification.medicationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

class NotificationEventFactory : EventFactory {
    override fun generateNotificationEvent(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ): NotificationEvent {
        return if (!medicationEvent.periodicityStatus()) {
            SingleMedicationEvent(
                medicationEvent,
                eventReminder,
                context)
        } else {
            RepeatingMedicationEvent(
                medicationEvent,
                eventReminder,
                context)
        }
    }
}