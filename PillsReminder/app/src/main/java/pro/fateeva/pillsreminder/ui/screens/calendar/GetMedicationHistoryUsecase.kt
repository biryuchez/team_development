package pro.fateeva.pillsreminder.ui.screens.calendar

interface GetMedicationHistoryUsecase {
    fun getMedicationHistory(): List<FakeMedicationHistoryEntity>
}