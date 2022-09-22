package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationDao
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder

class MedicationReminderRepositoryImpl(
    private val medicationDao: MedicationDao,
) : MedicationReminderRepository {

    private val medicationReminders = mutableListOf<MedicationReminder>()

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        medicationReminders.removeIf { it.id == medicationReminder.id }
        medicationReminders.add(medicationReminder)
        medicationDao.addMedicationReminder(
            MedicationReminderEntity(
                pillID = medicationReminder.id,
                medicationName = medicationReminder.medicationName,
                endDate = medicationReminder.endDate
            )
        )

        for (index in medicationReminder.medicationIntakes.indices) {
            medicationDao.addMedicationIntake(
                MedicationIntakeEntity(
                    intakeID = medicationReminder.id,
                    intakeIndex = index,
                    dosage = medicationReminder.medicationIntakes[index].dosage,
                    medicationTime = medicationReminder.medicationIntakes[index].time,
                )
            )
        }
    }

    override fun getMedicationReminder(id: Int): MedicationReminder {
        return medicationReminders.first {
            it.id == id
        }
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        return medicationReminders
    }

    override fun deleteMedicationReminder(id: Int) {
        medicationReminders.remove(getMedicationReminder(id))
    }
}