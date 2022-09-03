package pro.fateeva.pillsreminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pro.fateeva.pillsreminder.data.dao.PillDao
import pro.fateeva.pillsreminder.data.dao.entity.Fill

@Database(entities = [Fill::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getPillDao(): PillDao
}