package pro.fateeva.pillsreminder.ui.screens.twiceperday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TwicePerDaySettingsState(
    var firstMedicationReminderTime : Long = 0L,
    var firstMedicationDose: Int = 0,
    var secondMedicationReminderTime : Long = 0L,
    var secondMedicationDose: Int = 0
) : Parcelable
