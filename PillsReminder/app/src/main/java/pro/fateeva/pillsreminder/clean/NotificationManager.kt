package pro.fateeva.pillsreminder.clean

interface NotificationManager {
    fun planNotification(medicationReminder: MedicationReminder, medicationIntakeIndex : Int)
}