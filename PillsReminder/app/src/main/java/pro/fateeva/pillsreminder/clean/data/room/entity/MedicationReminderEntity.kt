package pro.fateeva.pillsreminder.clean.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MedicationReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val pillID: Int,
    val medicationName: String,
    val endDate: Long,
)