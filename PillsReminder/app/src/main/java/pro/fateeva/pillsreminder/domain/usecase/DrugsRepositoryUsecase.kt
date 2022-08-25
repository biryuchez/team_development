package pro.fateeva.pillsreminder.domain.usecase

import pro.fateeva.pillsreminder.domain.entity.DrugDomain

/**
 * Юзкейс предполагается для репозитория, который будет по API получать список лекарств
 * по поисковому запросу.
 * Скорректируйте при необходимости
 */
interface DrugsRepositoryUsecase {
    suspend fun findDrugs(drugNameQuery: String): List<DrugDomain>
}