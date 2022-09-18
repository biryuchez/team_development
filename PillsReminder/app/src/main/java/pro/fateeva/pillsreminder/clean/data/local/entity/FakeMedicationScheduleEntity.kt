package pro.fateeva.pillsreminder.clean.data.local.entity

data class FakeMedicationScheduleEntity(
    val id: Int = -1,
    val medicationTime: Long = -1,
    val pillId: Int = 8888,
    val pillName: String = "Анальгин",
    val medicationSuccessTime: Long = -1,
)