package pro.fateeva.pillsreminder.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pro.fateeva.pillsreminder.data.dao.entity.Fill
@Dao
interface PillDao {

    @Query("Select * FROM fill")
    fun getAll(): LiveData<List<Fill>>

    @Query("SELECT * FROM fill WHERE id = :id")
    fun getById(id: Long): Fill

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(fill: Fill)
}