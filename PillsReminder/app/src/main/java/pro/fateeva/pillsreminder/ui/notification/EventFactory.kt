package pro.fateeva.pillsreminder.ui.notification

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

/**
 * Фабрика классов системных событий о напоминаниях приема лекарства.
 * Формирует тип события в зависимости от типа события "Прием лекарства"
 */
interface EventFactory {
    fun generateNotificationEvent(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ): NotificationEvent

    class NotificationEventFactory : EventFactory {
        override fun generateNotificationEvent(
            medicationEvent: MedicationEventDomain,
            eventReminder: AlarmManager,
            context: Context,
        ): NotificationEvent {
            return if (!medicationEvent.periodicityStatus()) {
                NotificationEvent.Single(medicationEvent,
                    eventReminder,
                    context)
            } else {
                NotificationEvent.Repeating(medicationEvent,
                    eventReminder,
                    context)
            }
        }
    }
}