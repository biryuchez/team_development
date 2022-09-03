package pro.fateeva.pillsreminder.data.dao

import androidx.room.Insert
import androidx.room.Query
import pro.fateeva.pillsreminder.data.dao.entity.Fill

interface PillDao {

    @Query("Select * FROM fill")
    fun getAll(): List<Fill>

    @Query("SELECT * FROM fill WHERE id = :id")
    fun getById(id: Long): Fill

    @Insert
    fun add(fill: Fill)
}