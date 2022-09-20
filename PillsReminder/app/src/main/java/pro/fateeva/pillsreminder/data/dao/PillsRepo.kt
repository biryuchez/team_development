package pro.fateeva.pillsreminder.data.dao

import androidx.lifecycle.LiveData
import pro.fateeva.pillsreminder.data.dao.entity.Fill

class PillsRepo (private val pillDao: PillDao){
    val data: LiveData <List <Fill>> = pillDao.getAll()
    suspend fun insertFill (fill: Fill){
        pillDao.add(fill)
    }
}