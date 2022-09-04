package pro.fateeva.pillsreminder.clean

interface MedicationReminderRepository {
    fun saveMedicationReminder(medicationReminder: MedicationReminder)
    fun getMedicationReminder(id: Int) : MedicationReminder
}