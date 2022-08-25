package pro.fateeva.pillsreminder.domain.entity.medicationevent

class SingleMedicationEvent(
    ID: Int,
    drugName: String,
    dosage: String,
    firstMedicationTime: Long,
    isRepeatingEvent: Boolean = false,
) : MedicationEventDomain.BaseMedicationEvent(
    ID,
    drugName,
    dosage,
    isRepeatingEvent,
    firstMedicationTime)