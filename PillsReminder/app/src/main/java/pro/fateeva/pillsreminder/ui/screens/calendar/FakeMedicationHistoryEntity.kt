package pro.fateeva.pillsreminder.ui.screens.calendar

data class FakeMedicationHistoryEntity(
    val id: Int = -1,
    val medicationTime: Long,
    val pillName: String = "Анальгин",
    val medicationSuccessTime: Long = -1,
)