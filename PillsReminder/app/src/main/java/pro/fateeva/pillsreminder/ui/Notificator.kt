package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

/**
 * Контракт для MainActivity как сущности, которая генерирут уведомления
 */
interface Notificator {
    val manager: AlarmManager
    fun setNotification(medicineEvent: MedicationEventDomain)
    fun onNotificationClick()
}