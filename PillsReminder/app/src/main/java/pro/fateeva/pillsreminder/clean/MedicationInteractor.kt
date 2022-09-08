package pro.fateeva.pillsreminder.clean

import pro.fateeva.pillsreminder.extensions.copyDateFrom
import pro.fateeva.pillsreminder.extensions.toCalendar
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
            0
        )

        medicationReminderRepository.saveMedicationReminder(medicationReminder)
    }

    fun onNotificationShown(medicationReminder: MedicationReminder, previousReminderTime: Long) {
        updateRemindersTime(medicationReminder)

        val nextIndex = medicationReminder.medicationIntakes.indexOfFirst { it.time > previousReminderTime }

        if (nextIndex == -1) error("Next reminder not found")

        val nextMedicationReminderTime = medicationReminder.medicationIntakes[nextIndex].time

        if (nextMedicationReminderTime < medicationReminder.endDate) {
            notificationManager.planNotification(medicationReminder, nextIndex)
        }
    }

    fun getMedicationReminders() : List<MedicationReminder>{
        return medicationReminderRepository.getMedicationReminders()
    }

    private fun updateRemindersTime(medicationReminder: MedicationReminder) {
        medicationReminder.medicationIntakes = medicationReminder.medicationIntakes.map {
            addDayToMedicationReminder(it)
        }.sortedBy { it.time }
    }

    private fun addDayToMedicationReminder(medicationIntake: MedicationIntake): MedicationIntake {
        val calendarReminder = medicationIntake.time.toCalendar()
        if (medicationIntake.time < System.currentTimeMillis()) {
            calendarReminder.copyDateFrom(Calendar.getInstance())
            calendarReminder.add(Calendar.DAY_OF_MONTH, 1)
        }
        return medicationIntake
    }
}