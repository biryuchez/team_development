package pro.fateeva.pillsreminder.data.dao.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fill(
    @PrimaryKey val id: Long = 0,
    val name: String,
)