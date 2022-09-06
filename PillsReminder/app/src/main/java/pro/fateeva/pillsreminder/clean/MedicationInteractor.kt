package pro.fateeva.pillsreminder.clean

import pro.fateeva.pillsreminder.extensions.copyDateFrom
import pro.fateeva.pillsreminder.extensions.toCalendar
import pro.fateeva.pillsreminder.extensions.toCalendarDateOnly
import java.util.*

class MedicationInteractor(
    private val notificationManager: NotificationManager,
    private val medicationReminderRepository: MedicationReminderRepository
) {
    fun setMedicationReminder(quantityOfDays: Int, medicationReminder: MedicationReminder) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, quantityOfDays)
        medicationReminder.endDate = calendar.timeInMillis

        updateRemindersTime(medicationReminder)

        notificationManager.planNotification(
            medicationReminder,
            medicationReminder.remindersTime.first()
        )

        medicationReminderRepository.saveMedicationReminder(medicationReminder)
    }

    fun onNotificationShown(medicationReminder: MedicationReminder, previousReminderTime: Long) {
        updateRemindersTime(medicationReminder)
        val nextIndex = medicationReminder.remindersTime.indexOfFirst { it > previousReminderTime }

        if (nextIndex == -1) error("Next reminder not found")

        val nextMedicationReminderTime = medicationReminder.remindersTime[nextIndex]

        if (nextMedicationReminderTime < medicationReminder.endDate) {
            notificationManager.planNotification(medicationReminder, nextMedicationReminderTime)
        }
    }

    private fun updateRemindersTime(medicationReminder: MedicationReminder) {
        medicationReminder.remindersTime = medicationReminder.remindersTime.map {
            addDayToMedicationReminder(it)
        }.sorted()
    }

    private fun addDayToMedicationReminder(time: Long): Long {
        val calendarReminder = time.toCalendar()
        if (time < System.currentTimeMillis()) {
            calendarReminder.copyDateFrom(Calendar.getInstance())
            calendarReminder.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendarReminder.timeInMillis
    }
}