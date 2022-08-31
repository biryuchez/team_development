package pro.fateeva.pillsreminder.ui.screens.pillsearching

import pro.fateeva.pillsreminder.domain.entity.DrugDomain

sealed class SearchPillState {
    object Loading: SearchPillState()
    class Success(dataList: List<DrugDomain>): SearchPillState()
    class Error(error: String): SearchPillState()
}
