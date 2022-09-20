package pro.fateeva.pillsreminder.data.dao

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.fateeva.pillsreminder.data.AppDataBase
import pro.fateeva.pillsreminder.data.dao.entity.Fill

class PillsViewModel (application: Application): AndroidViewModel(application) {
    val pillsData: LiveData<List<Fill>>
    private val repo: PillsRepo
    init {
        val dao = AppDataBase.getDatabase(application).getPillDao()
        repo = PillsRepo(dao)
        pillsData = repo.data
    }
    fun AddPill (fill: Fill){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertFill(fill)
        }
    }
}