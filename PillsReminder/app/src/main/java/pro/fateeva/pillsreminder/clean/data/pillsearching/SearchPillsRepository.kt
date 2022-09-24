package pro.fateeva.pillsreminder.clean.data.pillsearching

import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

/**
 * Юзкейс предполагается для репозитория, который будет по API получать список лекарств
 * по поисковому запросу.
 * Скорректируйте при необходимости
 */
interface SearchPillsRepository {
    suspend fun searchPills(query: String): List<DrugDomain>
}