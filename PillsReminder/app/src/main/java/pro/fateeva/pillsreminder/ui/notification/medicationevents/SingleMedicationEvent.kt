package pro.fateeva.pillsreminder.ui.notification.medicationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

class SingleMedicationEvent(
    medicationEvent: MedicationEventDomain,
    eventReminder: AlarmManager,
    context: Context,
) : BasicNotificationEvent(medicationEvent, eventReminder, context) {
    override fun setEvent() {
        eventReminder.setExact(
            AlarmManager.RTC_WAKEUP,
            medicationEvent.firstMedicationTime(),
            pendingEventIntent)
    }
}