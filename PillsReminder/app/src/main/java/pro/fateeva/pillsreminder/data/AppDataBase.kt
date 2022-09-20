package pro.fateeva.pillsreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pro.fateeva.pillsreminder.data.dao.PillDao
import pro.fateeva.pillsreminder.data.dao.entity.Fill

@Database(entities = [Fill::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getPillDao(): PillDao
    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "AppDataBase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}