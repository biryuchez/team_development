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
        // временная заглушка - пока не реализован функционал повторяющичся событий,
        // будет формироваться однократное напоминание
        systemAlarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            medicationEvent.firstMedicationTime(),
            pendingEventIntent)
    }
}