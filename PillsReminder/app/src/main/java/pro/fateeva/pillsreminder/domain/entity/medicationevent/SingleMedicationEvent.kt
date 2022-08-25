package pro.fateeva.pillsreminder.domain.entity.medicationevent

/**
 * Класс, отвечающий за событие "Одиночный прием лекарства"
 */
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