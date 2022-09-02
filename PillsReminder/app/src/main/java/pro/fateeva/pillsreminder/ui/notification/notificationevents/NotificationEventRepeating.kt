package pro.fateeva.pillsreminder.ui.notification.notificationevents

import android.app.AlarmManager
import android.content.Context
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

class NotificationEventRepeating(
    medicationEvent: MedicationEventDomain,
    systemAlarmManager: AlarmManager,
    context: Context,
) : NotificationEventBase(medicationEvent, systemAlarmManager, context) {
    override fun setEvent() {
        TODO("Not yet implemented")
    }
}