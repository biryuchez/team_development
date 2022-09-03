package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener

/**
 * Контракт для MainActivity как сущности, которая генерирут уведомления
 */
interface NotificationHandler {
    val alarmManager: AlarmManager
    val actionListener: NotificationActionListener
    fun setNotification(medicineEvent: MedicationEventDomain)
    fun onGetDrugAction(message: String)
    fun onCancelDrugAction(message: String)
}