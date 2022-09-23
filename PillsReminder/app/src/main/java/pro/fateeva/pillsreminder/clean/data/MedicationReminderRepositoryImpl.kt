package pro.fateeva.pillsreminder.clean.data

import pro.fateeva.pillsreminder.clean.data.room.MedicationDao
import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import java.util.*

private const val ONE_DAY_IN_MILLIS = 86_400_000L

class MedicationReminderRepositoryImpl(
    private val medicationDao: MedicationDao,
    private val mapper: MedicationEntityMapper,
) : MedicationReminderRepository {

    override fun saveMedicationReminder(
        medicationReminder: MedicationReminder,
        quantityOfDays: Int,
    ) {
        medicationDao.deleteAllIntakesById(medicationReminder.id)

        medicationDao.addMedicationReminder(
            mapper.mapMedicationReminderDomainToEntity(medicationReminder)
        )

        repeat(quantityOfDays) { dayOffset ->
            for (index in medicationReminder.medicationIntakes.indices) {
                medicationDao.addMedicationIntake(
                    mapper.createMedicationIntakeEntity(medicationReminder, index, dayOffset)
                )
            }
        }
    }

    override fun getMedicationReminder(id: Int): MedicationReminder {
        val intakeIndexSet = mutableSetOf<Int>()
        val intakeEntityList = medicationDao.getMedicationIntakes(id)

        for (intake in intakeEntityList) {
            intakeIndexSet.add(intake.intakeIndex)
        }

        return mapper.mapMedicationReminderEntityToDomain(
            medicationDao.getMedicationReminder(id),
            medicationDao.getMedicationIntakes(id, intakeIndexSet.size)
        )
    }

    override fun getMedicationReminders(): List<MedicationReminder> {
        val medicationRemindersList = mutableListOf<MedicationReminder>()

        for (medicationReminderEntity in medicationDao.getAllMedicationReminders()) {
            val intakeIndexSet = mutableSetOf<Int>()
            val intakeEntityList =
                medicationDao.getMedicationIntakes(medicationReminderEntity.pillID)

            for (intake in intakeEntityList) {
                intakeIndexSet.add(intake.intakeIndex)
            }

            medicationRemindersList.add(
                mapper.mapMedicationReminderEntityToDomain(
                    medicationReminderEntity,
                    medicationDao.getMedicationIntakes(medicationReminderEntity.pillID,
                        intakeIndexSet.size)
                )
            )
        }

        return medicationRemindersList
    }


    override fun deleteMedicationReminder(id: Int) {
        medicationDao.deleteMedicationReminder(id)
        medicationDao.deleteMedicationIntake(id)
    }

    override fun getCalendarData(): List<MedicationScheduleItemDomain> {
        val scheduleList = mutableListOf<MedicationScheduleItemDomain>()
        for (intake in medicationDao.getAllMedicationIntakes()) {
            scheduleList.add(mapper.mapToMedicationScheduleItemDomain(
                intake,
                medicationDao.getMedicationReminder(intake.intakeID).medicationName)
            )
        }
        return scheduleList
    }

    /**
     * метод, обновляющий отредактированные данные в medicationReminder.
     * Для корректного обновления из БД удаляются все medicationIntakes, время которых
     * еще не прошло.
     */
    override fun updateMedicationReminder(medicationReminder: MedicationReminder) {
        val calendar = Calendar.getInstance()
        val timeDifference = medicationReminder.endDate - calendar.timeInMillis
        val dayOffset = ((timeDifference + ONE_DAY_IN_MILLIS) / ONE_DAY_IN_MILLIS).toInt()

        medicationDao.deletePlannedIntakes(medicationReminder.id, calendar.timeInMillis)

        repeat(dayOffset) { dayIndex ->
            for (index in medicationReminder.medicationIntakes.indices) {
                medicationDao.addMedicationIntake(
                    mapper.createMedicationIntakeEntity(medicationReminder, index, dayIndex)
                )
            }
        }
    }
}