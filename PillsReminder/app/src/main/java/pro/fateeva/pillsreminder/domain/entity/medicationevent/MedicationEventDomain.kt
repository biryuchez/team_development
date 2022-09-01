package pro.fateeva.pillsreminder.domain.entity.medicationevent

/**
 * Интерфейс и абстрактный класс, описывающие основные поля и функции события "Прием лекарства".
 * Поля абстрактного класса приватные, доступ к ним снаружи через функции, названия которых
 * повторяют имена полей.
 */
interface MedicationEventDomain {

    fun id(): Int
    fun drugName(): String
    fun dosage(): Int
    fun firstMedicationTime(): Long
    fun medicationDaysCount(): Int

    class MedicationEvent(
        private val ID: Int,
        private val drugName: String,
        private val dosage: Int,
        private val firstMedicationTime: Long,
        private val medicationDaysCount: Int = 0,
    ) : MedicationEventDomain {
        override fun id() = ID
        override fun drugName() = drugName
        override fun dosage() = dosage
        override fun firstMedicationTime() = firstMedicationTime
        override fun medicationDaysCount() = medicationDaysCount
    }
}

