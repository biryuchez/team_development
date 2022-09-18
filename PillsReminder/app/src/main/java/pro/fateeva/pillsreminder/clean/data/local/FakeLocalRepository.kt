package pro.fateeva.pillsreminder.clean.data.local

import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

interface FakeLocalRepository {
    fun getMedicationSchedule(): List<MedicationScheduleItemDomain>
}