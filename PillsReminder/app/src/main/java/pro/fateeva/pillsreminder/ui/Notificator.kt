package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

interface Notificator {
    val manager: AlarmManager
    fun setNotification(medicineEvent: MedicationEventDomain)
    fun onNotificationClick()
}