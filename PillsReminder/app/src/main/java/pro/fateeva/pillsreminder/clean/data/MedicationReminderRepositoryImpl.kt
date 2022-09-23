package pro.fateeva.pillsreminder.clean.data

import android.util.Log
import pro.fateeva.pillsreminder.clean.data.room.MedicationDao
import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import java.text.SimpleDateFormat
import java.util.*

private const val ONE_DAY_IN_MILLIS = 86_400_000L

class MedicationReminderRepositoryImpl(
    private val medicationDao: MedicationDao,
    private val mapper: MedicationEntityMapper,
) : MedicationReminderRepository {

    override fun saveMedicationReminder(medicationReminder: MedicationReminder) {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        medicationDao.deletePlannedIntakes(medicationReminder.id, currentTime)

        medicationDao.addMedicationReminder(
            mapper.mapMedicationReminderDomainToEntity(medicationReminder)
        )

        for (index in medicationReminder.medicationIntakes.indices) {

            var medicationTime = medicationReminder.medicationIntakes[index].time

            while (medicationTime < medicationReminder.endDate) {
                medicationDao.addMedicationIntake(
                    mapper.createMedicationIntakeEntity(medicationReminder, index, medicationTime)
                )
                medicationTime += ONE_DAY_IN_MILLIS
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
            scheduleList.add(mapper.mapToMedicationScheduleItemDomain(intake))
        }
        return scheduleList
    }
}