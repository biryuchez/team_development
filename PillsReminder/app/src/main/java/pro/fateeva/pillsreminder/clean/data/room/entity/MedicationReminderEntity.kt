package pro.fateeva.pillsreminder.clean.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MedicationReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pillID: Int,
    val medicationName: String,
    val endDate: Long,
)