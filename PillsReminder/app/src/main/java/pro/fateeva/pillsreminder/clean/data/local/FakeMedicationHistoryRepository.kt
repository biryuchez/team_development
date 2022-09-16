package pro.fateeva.pillsreminder.clean.data.local

import pro.fateeva.pillsreminder.clean.data.local.entity.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.clean.domain.usecase.GetMedicationScheduleUsecase
import pro.fateeva.pillsreminder.clean.domain.MedicationHistoryRepository

class FakeMedicationHistoryRepository(
    private val localDataSource: GetMedicationScheduleUsecase,
) : MedicationHistoryRepository {

    override fun getMedicationSchedule(): List<FakeMedicationScheduleEntity> {
        return localDataSource.getMedicationSchedule()
    }
}