package pro.fateeva.pillsreminder.clean.domain.usecase

import pro.fateeva.pillsreminder.clean.data.local.entity.FakeMedicationScheduleEntity

interface GetMedicationScheduleUsecase {
    fun getMedicationSchedule(): List<FakeMedicationScheduleEntity>
}