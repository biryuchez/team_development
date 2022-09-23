package pro.fateeva.pillsreminder.clean.data.room

import pro.fateeva.pillsreminder.clean.data.room.fake.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationIntake
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

class MedicationEntityMapper {
    fun mapToMedicationScheduleItemDomain(
        medicationIntakeEntity: MedicationIntakeEntity,
        name: String,
    ): MedicationScheduleItemDomain {
        return with(medicationIntakeEntity) {
            MedicationScheduleItemDomain(
                pillId = intakeID,
                medicationTime = medicationTime,
                pillName = name,
                actualMedicationTime = actualMedicationTime
            )
        }
    }

    fun mapMedicationReminderDomainToEntity(medicationReminder: MedicationReminder): MedicationReminderEntity {
        return MedicationReminderEntity(
            pillID = medicationReminder.id,
            medicationName = medicationReminder.medicationName,
            endDate = medicationReminder.endDate
        )
    }

    fun mapMedicationReminderEntityToDomain(
        medicationReminderEntity: MedicationReminderEntity,
        medicationIntakeEntityList: List<MedicationIntakeEntity>,
    ): MedicationReminder {
        return MedicationReminder(
            id = medicationReminderEntity.pillID,
            medicationName = medicationReminderEntity.medicationName,
            medicationIntakes = medicationIntakeEntityList.map {
                mapMedicationIntakeEntityToDomain((it))
            },
            endDate = medicationReminderEntity.endDate
        )
    }

    private fun mapMedicationIntakeEntityToDomain(
        medicationIntakeEntity: MedicationIntakeEntity,
    ): MedicationIntake {
        return MedicationIntake(
            dosage = medicationIntakeEntity.dosage,
            time = medicationIntakeEntity.medicationTime
        )
    }

    fun createMedicationIntakeEntity(
        medicationReminder: MedicationReminder,
        index: Int,
        dayOffset: Int
    ): MedicationIntakeEntity {
        return MedicationIntakeEntity(
            intakeID = medicationReminder.id,
            intakeIndex = index,
            dosage = medicationReminder.medicationIntakes[index].dosage,
            medicationTime = medicationReminder.medicationIntakes[index].time + (86400000 * dayOffset),
        )
    }
}