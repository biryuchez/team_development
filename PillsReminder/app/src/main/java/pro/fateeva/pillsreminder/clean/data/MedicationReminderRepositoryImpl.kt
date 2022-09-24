package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationReminderDao
import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.data.room.MedicationIntakeDao
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import pro.fateeva.pillsreminder.extensions.getDayBeginningTime
import pro.fateeva.pillsreminder.extensions.getDayEndTime
import java.util.*

private const val ONE_DAY_IN_MILLIS = 86_400_000L

class MedicationReminderRepositoryImpl(
    private val reminderDao: MedicationReminderDao,
    private val intakeDao: MedicationIntakeDao,
    private val mapper: MedicationEntityMapper,
) : MedicationReminderRepository {

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        intakeDao.deletePlannedIntakes(medicationReminder.id, currentTime)

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
        val calendar = Calendar.getInstance()

        return mapper.mapMedicationReminderEntityToDomain(
            reminderDao.getMedicationReminder(id),
            intakeDao.getMedicationIntakes(
                id,
                calendar.getDayBeginningTime(),
                calendar.getDayEndTime()
            )
        )
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        val calendar = Calendar.getInstance()

        val medicationRemindersList = mutableListOf<MedicationReminder>()

        for (medicationReminderEntity in reminderDao.getAllMedicationReminders()) {
            medicationRemindersList.add(
                mapper.mapMedicationReminderEntityToDomain(
                    medicationReminderEntity,
                    intakeDao.getMedicationIntakes(
                        medicationReminderEntity.pillID,
                        calendar.getDayBeginningTime(),
                        calendar.getDayEndTime()
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