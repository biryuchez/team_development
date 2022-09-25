package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.data.room.MedicationIntakeDao
import pro.fateeva.pillsreminder.clean.data.room.MedicationReminderDao
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

private const val ONE_DAY_IN_MILLIS = 86_400_000L

class MedicationReminderRepositoryImpl(
    private val reminderDao: MedicationReminderDao,
    private val intakeDao: MedicationIntakeDao,
    private val mapper: MedicationEntityMapper,
) : MedicationReminderRepository {

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        intakeDao.deletePlannedIntakes(medicationReminder.id, System.currentTimeMillis())

        reminderDao.addMedicationReminder(
            mapper.mapMedicationReminderDomainToEntity(medicationReminder)
        )

        for (index in medicationReminder.medicationIntakes.indices) {

            var medicationTime = medicationReminder.medicationIntakes[index].time

            while (medicationTime < medicationReminder.endDate) {
                intakeDao.addMedicationIntake(
                    mapper.mapMedicationReminderToIntakeEntity(
                        medicationReminder,
                        index,
                        medicationTime
                    )
                )
                medicationTime += ONE_DAY_IN_MILLIS
            }
        }
    }

    override fun getMedicationReminder(id: Int): MedicationReminder {
        return mapper.mapMedicationReminderEntityToDomain(
            reminderDao.getMedicationReminder(id),
            intakeDao.getMedicationIntakesForNext24H(
                id,
                System.currentTimeMillis()
            )
        )
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        val medicationRemindersList = mutableListOf<MedicationReminder>()

        for (medicationReminderEntity in reminderDao.getAllMedicationReminders()) {
            medicationRemindersList.add(
                mapper.mapMedicationReminderEntityToDomain(
                    medicationReminderEntity,
                    intakeDao.getMedicationIntakesForNext24H(
                        medicationReminderEntity.pillID,
                        System.currentTimeMillis()
                    )
                )
            )
        }

        return medicationRemindersList
    }

    override fun deleteMedicationReminder(id: Int) {
        reminderDao.deleteMedicationReminder(id)
        intakeDao.deleteMedicationIntake(id)
    }

    override fun getCalendarData(): List<MedicationScheduleItemDomain> {
        val scheduleList = mutableListOf<MedicationScheduleItemDomain>()
        for (intake in intakeDao.getAllMedicationIntakes()) {
            scheduleList.add(mapper.mapToMedicationScheduleItemDomain(intake))
        }
        return scheduleList
    }
}