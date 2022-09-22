package pro.fateeva.pillsreminder.clean.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMedicationReminder(medicationReminder: MedicationReminderEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMedicationIntake(medicationIntake: MedicationIntakeEntity)
}