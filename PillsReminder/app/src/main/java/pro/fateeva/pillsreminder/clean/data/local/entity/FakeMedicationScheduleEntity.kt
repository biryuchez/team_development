package pro.fateeva.pillsreminder.clean.data.local.entity

data class FakeMedicationScheduleEntity(
    val id: Int = -1,
    val medicationTime: Long,
    val pillName: String = "Анальгин",
    val medicationSuccessTime: Long = -1,
)