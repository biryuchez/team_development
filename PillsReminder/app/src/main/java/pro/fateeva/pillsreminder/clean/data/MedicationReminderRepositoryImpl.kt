package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationDao
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationIntake
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
        val medicationReminderEntity = medicationDao.getMedicationReminder(id)
        val medicationIntakeEntityList = medicationDao.getMedicationIntakes(id)
        return MedicationReminder(
            id = medicationReminderEntity.pillID,
            medicationName = medicationReminderEntity.medicationName,
            medicationIntakes = medicationIntakeEntityList.map {
                MedicationIntake(
                    dosage = it.dosage,
                    time = it.medicationTime
                )
            },
            endDate = medicationReminderEntity.endDate
        )
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        val medicationReminderEntityList = medicationDao.getAllMedicationReminders()
        val medicationRemindersList = mutableListOf<MedicationReminder>()
        for (medicationReminderEntity in medicationReminderEntityList) {
            val medicationIntakeEntityList =
                medicationDao.getMedicationIntakes(medicationReminderEntity.pillID)
            medicationRemindersList.add(
                MedicationReminder(
                    id = medicationReminderEntity.pillID,
                    medicationName = medicationReminderEntity.medicationName,
                    medicationIntakes = medicationIntakeEntityList.map {
                        MedicationIntake(
                            dosage = it.dosage,
                            time = it.medicationTime
                        )
                    },
                    endDate = medicationReminderEntity.endDate
                )
            )
        }
        return medicationRemindersList
    }

    override fun deleteMedicationReminder(id: Int) {
        medicationReminders.remove(getMedicationReminder(id))
    }
}