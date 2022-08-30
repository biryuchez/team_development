package pro.fateeva.pillsreminder.ui.notification.medicationevents

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
}