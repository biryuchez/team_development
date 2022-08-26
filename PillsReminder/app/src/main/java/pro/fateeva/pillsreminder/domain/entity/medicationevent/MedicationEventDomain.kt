package pro.fateeva.pillsreminder.domain.entity.medicationevent

/**
 * Интерфейс и абстрактный класс, описывающие основные поля и функции события "Прием лекарства".
 * Поля абстрактного класса приватные, доступ к ним снаружи через функции, названия которых
 * повторяют имена полей.
 */
interface MedicationEventDomain {

    fun id(): Int
    fun drugName(): String
    fun dosage(): String
    fun periodicityStatus(): Boolean
    fun firstMedicationTime(): Long
    fun medicationPeriod(): Long
    fun medicationInterval(): Long

    abstract class BaseMedicationEvent(
        private val ID: Int,
        private val drugName: String,
        private val dosage: String,
        private val isRepeatingEvent: Boolean,
        private val firstMedicationTime: Long,
        private val medicationPeriod: Long = 0,
        private val medicationInterval: Long = 0,
    ) : MedicationEventDomain {
        override fun id() = ID
        override fun drugName() = drugName
        override fun dosage() = dosage
        override fun periodicityStatus() = isRepeatingEvent
        override fun firstMedicationTime() = firstMedicationTime
        override fun medicationPeriod() = medicationPeriod
        override fun medicationInterval() = medicationInterval
    }

    /**
     * Класс, отвечающий за событие "Одиночный прием лекарства"
     */
    class Single(
        ID: Int,
        drugName: String,
        dosage: String,
        firstMedicationTime: Long,
        isRepeatingEvent: Boolean = false,
    ) : BaseMedicationEvent(
        ID,
        drugName,
        dosage,
        isRepeatingEvent,
        firstMedicationTime)

    /**
     * Класс, отвечающий за событие "Повторяющийся прием лекарства"
     */
    class Repeating(
        ID: Int,
        pillName: String,
        dosage: String,
        firstMedicationTime: Long,
        medicationPeriod: Long,
        medicationInterval: Long,
        isRepeatingEvent: Boolean = true,
    ) : BaseMedicationEvent(
        ID,
        pillName,
        dosage,
        isRepeatingEvent,
        firstMedicationTime,
        medicationPeriod,
        medicationInterval)
}

