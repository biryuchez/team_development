package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder

interface MedicationReminderRepository {
    fun saveMedicationReminder(medicationReminder: MedicationReminder)
    fun getMedicationReminder(id: Int) : MedicationReminder
    fun getMedicationReminders(): List<MedicationReminder>
}