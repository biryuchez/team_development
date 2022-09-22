package pro.fateeva.pillsreminder.clean.data.room

import pro.fateeva.pillsreminder.clean.data.room.entity.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

class MapperEntityToDomain {
    fun convertToMedicationScheduleItemDomain(
        fakeItem: FakeMedicationScheduleEntity
    ): MedicationScheduleItemDomain {
        return with(fakeItem) {
            MedicationScheduleItemDomain(
                id = id,
                medicationTime = medicationTime,
                pillId = pillId,
                pillName = pillName,
                medicationSuccessTime = medicationSuccessTime
            )
        }
    }
}