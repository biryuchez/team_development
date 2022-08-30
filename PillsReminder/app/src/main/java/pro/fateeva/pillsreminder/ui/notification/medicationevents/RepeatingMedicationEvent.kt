package pro.fateeva.pillsreminder.ui.notification.medicationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

class RepeatingMedicationEvent(
    medicationEvent: MedicationEventDomain,
    eventReminder: AlarmManager,
    context: Context,
) : BasicNotificationEvent(medicationEvent, eventReminder, context) {
    override fun setEvent() {
        TODO("Not yet implemented")
        // тут будет реализовано повторяющееся напоминание;
        // сейчас не реализовано, т.к. повторяющиеся события надо программно отменять
    }
}