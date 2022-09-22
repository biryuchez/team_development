package pro.fateeva.pillsreminder.clean.data.room

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

interface FakeLocalRepository {
    fun getMedicationSchedule(): List<MedicationScheduleItemDomain>
}