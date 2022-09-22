package pro.fateeva.pillsreminder.clean.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(
    value = ["intakeID", "intakeIndex"],
    unique = true)])
class MedicationIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val intakeID: Int,
    val intakeIndex: Int,
    val dosage: Int,
    val medicationTime: Long,
    val actualMedicationTime: Long = -1,
)