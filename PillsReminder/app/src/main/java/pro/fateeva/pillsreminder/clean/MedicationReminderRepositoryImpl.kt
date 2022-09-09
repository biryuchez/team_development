package pro.fateeva.pillsreminder.clean

import java.util.*

class MedicationReminderRepositoryImpl : MedicationReminderRepository {

    private val medicationReminders = mutableListOf<MedicationReminder>(
//        MedicationReminder(
//            0,
//            "Test",
//            listOf(
//                MedicationIntake(2, 1662757200000), MedicationIntake(3, 1662760800000)
//            ),
//            endDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }.timeInMillis
//        )
    )

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