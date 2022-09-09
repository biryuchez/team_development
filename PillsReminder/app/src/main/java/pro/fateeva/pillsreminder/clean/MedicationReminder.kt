package pro.fateeva.pillsreminder.clean

data class MedicationReminder(
    val id: Int,
    val medicationName: String,
    var medicationIntakes: List<MedicationIntake>,
    var endDate: Long = -1
)