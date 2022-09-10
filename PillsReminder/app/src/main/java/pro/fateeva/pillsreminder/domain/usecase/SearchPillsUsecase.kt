package pro.fateeva.pillsreminder.domain.usecase

import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

/**
 * Юзкейс предполагается для репозитория, который будет по API получать список лекарств
 * по поисковому запросу.
 * Скорректируйте при необходимости
 */
interface SearchPillsUsecase {
    suspend fun searchPills(query: String): List<DrugDomain>
}