package pro.fateeva.pillsreminder.domain.entity.medicationevent

class RepeatingMedicationEvent(
    ID: Int,
    pillName: String,
    dosage: String,
    firstMedicationTime: Long,
    medicationPeriod: Long,
    medicationInterval: Long,
    isRepeatingEvent: Boolean = true,
) : MedicationEventDomain.BaseMedicationEvent(
    ID,
    pillName,
    dosage,
    isRepeatingEvent,
    firstMedicationTime,
    medicationPeriod,
    medicationInterval)