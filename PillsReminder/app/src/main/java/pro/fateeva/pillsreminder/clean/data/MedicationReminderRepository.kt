package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

interface MedicationReminderRepository {
    fun saveMedicationReminder(medicationReminder: MedicationReminder, quantityOfDays: Int)
    fun getMedicationReminder(id: Int) : MedicationReminder
    fun getMedicationReminders(): List<MedicationReminder>
    fun deleteMedicationReminder(id: Int)
    fun getCalendarData(): List<MedicationScheduleItemDomain>
    fun updateMedicationReminder(medicationReminder: MedicationReminder)
}