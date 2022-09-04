package pro.fateeva.pillsreminder.clean

data class MedicationReminder(
    val id: Int,
    val medicationName: String,
    val dosage: Int,
    var remindersTime: List<Long>,
    var endDate: Long = -1
)