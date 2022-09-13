package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OncePerDaySettingsState(
    var medicationName : String = "",
    var medicationReminderTime : Long = 0L,
    var medicationDose: Int = 0
) : Parcelable
