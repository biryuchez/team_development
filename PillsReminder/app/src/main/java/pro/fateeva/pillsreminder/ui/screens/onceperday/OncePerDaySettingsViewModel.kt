package pro.fateeva.pillsreminder.ui.screens.onceperday

import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.MedicationInteractor
import pro.fateeva.pillsreminder.clean.MedicationReminder

class OncePerDaySettingsViewModel(
    private val interactor: MedicationInteractor
) : ViewModel() {

    fun setMedicationReminder(quantityOfDays: Int, medicationReminder: MedicationReminder) {
        interactor.setMedicationReminder(quantityOfDays, medicationReminder)
    }

}