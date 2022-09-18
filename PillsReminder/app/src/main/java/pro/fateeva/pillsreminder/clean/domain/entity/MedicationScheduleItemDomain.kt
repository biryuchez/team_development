package pro.fateeva.pillsreminder.clean.domain.entity

data class MedicationScheduleItemDomain(
    val id: Int,
    val medicationTime: Long,
    val pillId: Int,
    val pillName: String,
    val medicationSuccessTime: Long,
)