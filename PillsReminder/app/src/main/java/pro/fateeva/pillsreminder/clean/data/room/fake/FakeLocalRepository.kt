package pro.fateeva.pillsreminder.clean.data.room.fake

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

interface FakeLocalRepository {
    fun getMedicationSchedule(): List<MedicationScheduleItemDomain>
}