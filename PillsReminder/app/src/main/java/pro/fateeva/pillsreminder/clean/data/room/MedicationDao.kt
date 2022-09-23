package pro.fateeva.pillsreminder.clean.data.room

import androidx.room.*
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationIntakeEntity
import pro.fateeva.pillsreminder.clean.data.room.entity.MedicationReminderEntity

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMedicationReminder(medicationReminder: MedicationReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMedicationIntake(medicationIntake: MedicationIntakeEntity)

    @Query("SELECT * FROM MedicationReminderEntity")
    fun getAllMedicationReminders(): List<MedicationReminderEntity>

    @Query("SELECT * FROM MedicationIntakeEntity")
    fun getAllMedicationIntakes(): List<MedicationIntakeEntity>

    @Query("SELECT * FROM MedicationReminderEntity WHERE pillID = :pillID")
    fun getMedicationReminder(pillID: Int): MedicationReminderEntity

    @Query("SELECT * FROM MedicationIntakeEntity WHERE intakeID = :intakeID ORDER BY medicationTime ASC")
    fun getMedicationIntakes(intakeID: Int): List<MedicationIntakeEntity>

    @Query("SELECT * FROM MedicationIntakeEntity WHERE intakeID = :intakeID ORDER BY medicationTime DESC LIMIT :limit")
    fun getMedicationIntakes(intakeID: Int, limit: Int): List<MedicationIntakeEntity>

    @Query("DELETE FROM MedicationReminderEntity WHERE pillID = :pillID")
    fun deleteMedicationReminder(pillID: Int)

    @Query("DELETE FROM MedicationIntakeEntity WHERE intakeID = :pillID")
    fun deleteMedicationIntake(pillID: Int)

    @Query("DELETE FROM MedicationIntakeEntity WHERE intakeID == :id")
    fun deleteAllIntakesById(id: Int)

    @Query("DELETE FROM MedicationIntakeEntity  WHERE intakeID == :id AND medicationTime > :currentTime")
    fun deletePlannedIntakes(id: Int, currentTime: Long)
}