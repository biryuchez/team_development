package pro.fateeva.pillsreminder.ui.notification.notificationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

/**
 * Фабрика классов системных событий о напоминаниях приема лекарства.
 * Когда пользователь указывет, какое лекарство и с какой периодичностью надо принимать,
 * формируется объект типа MedicationEventDomain с параметрами, которые указал пользователь.
 * На основании параметров этого объекта генерируется объект типа NotificationEvent, в котором,
 * в зависимости от типа (Single/Repeating) переопределяется метод setEvent(),
 * создающий "будильник" в системном классе AlarmManager.
 */
class NotificationEventFactory : EventFactory {
    override fun generateNotificationEvent(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ): NotificationEvent {
        return if (!medicationEvent.periodicityStatus()) {
            NotificationEventSingle(
                medicationEvent,
                eventReminder,
                context)
        } else {
            NotificationEventRepeating(
                medicationEvent,
                eventReminder,
                context)
        }
    }
}