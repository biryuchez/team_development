package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationDao
import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder

class MedicationReminderRepositoryImpl(
    private val medicationDao: MedicationDao,
    private val mapper: MedicationEntityMapper,
) : MedicationReminderRepository {

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        medicationDao.addMedicationReminder(
            mapper.mapMedicationReminderDomainToEntity(medicationReminder)
        )

        for (index in medicationReminder.medicationIntakes.indices) {
            medicationDao.addMedicationIntake(
                mapper.createMedicationIntakeEntity(medicationReminder, index)
            )
        }
    }

    override fun getMedicationReminder(id: Int): MedicationReminder {
        return mapper.mapMedicationReminderEntityToDomain(
            medicationDao.getMedicationReminder(id),
            medicationDao.getMedicationIntakes(id)
        )
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        val medicationRemindersList = mutableListOf<MedicationReminder>()

        for (medicationReminderEntity in medicationDao.getAllMedicationReminders()) {
            medicationRemindersList.add(
                mapper.mapMedicationReminderEntityToDomain(
                    medicationReminderEntity,
                    medicationDao.getMedicationIntakes(medicationReminderEntity.pillID)
                )
            )
        }

        return medicationRemindersList
    }

    override fun deleteMedicationReminder(id: Int) {
        medicationDao.deleteMedicationReminder(id)
        medicationDao.deleteMedicationIntake(id)
    }
}