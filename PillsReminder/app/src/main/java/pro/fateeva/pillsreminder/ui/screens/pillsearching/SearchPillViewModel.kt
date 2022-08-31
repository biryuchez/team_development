package pro.fateeva.pillsreminder.ui.screens.pillsearching

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchPillViewModel : ViewModel() {
    private val liveData = MutableLiveData<SearchPillState>()

    fun getData(): LiveData<SearchPillState> = liveData
}