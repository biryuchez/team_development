package pro.fateeva.pillsreminder.domain.usecase

import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

/**
 * Юзкейс предполагается для репозитория, который будет из Room получать список
 * текущих (установленных) напоминаний о приеме лекарств.
 * Скорректируйте при необходимости
 */
interface MedicationEventsRepositoryUsecase {
    suspend fun getNotificationEvents(): List<MedicationEventDomain>
}