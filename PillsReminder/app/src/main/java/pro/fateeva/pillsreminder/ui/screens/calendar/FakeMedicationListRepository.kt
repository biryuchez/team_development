package pro.fateeva.pillsreminder.ui.screens.calendar

class FakeMedicationListRepository(private val localDataSource: GetMedicationHistoryUsecase) {
    fun getMedicationList(): List<FakeMedicationHistoryEntity> {
        return localDataSource.getMedicationHistory()
    }
}