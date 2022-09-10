package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder

class MedicationReminderRepositoryImpl : MedicationReminderRepository {

    private val medicationReminders = mutableListOf<MedicationReminder>()

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        medicationReminders.removeIf { it.id == medicationReminder.id }
        medicationReminders.add(medicationReminder)
    }

    override fun getMedicationReminder(id: Int): MedicationReminder {
        return medicationReminders.first {
            it.id == id
        }
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        return medicationReminders
    }
}