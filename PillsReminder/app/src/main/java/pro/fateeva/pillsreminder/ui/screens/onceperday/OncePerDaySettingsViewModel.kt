package pro.fateeva.pillsreminder.ui.screens.onceperday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.MedicationIntake
import pro.fateeva.pillsreminder.clean.MedicationInteractor
import pro.fateeva.pillsreminder.clean.MedicationReminder
import pro.fateeva.pillsreminder.domain.entity.DrugDomain

class OncePerDaySettingsViewModel(
    private val handle: SavedStateHandle,
    private val interactor: MedicationInteractor
) : ViewModel() {

    private val liveData: MutableLiveData<OncePerDaySettingsState> =
        handle.getLiveData("state", OncePerDaySettingsState())
    private val medicationTimeError = MutableLiveData(false)
    private val medicationDoseError = MutableLiveData(false)

    private val oncePerDaySettingsState: OncePerDaySettingsState
        get() = liveData.value ?: OncePerDaySettingsState()

    val hasMedicationTimeError: LiveData<Boolean>
        get() = medicationTimeError

    val hasMedicationDoseError: LiveData<Boolean>
        get() = medicationDoseError

    private fun isEveryFieldValid() = oncePerDaySettingsState.let {
        it.medicationDose > 0 && it.medicationReminderTime > 0
    }

    fun setMedicationReminder(quantityOfDays: Int, selectedDrug : DrugDomain) {
        val medicationReminder = MedicationReminder(
            selectedDrug.ID,
            selectedDrug.drugName,
            listOf(MedicationIntake(oncePerDaySettingsState.medicationDose,oncePerDaySettingsState.medicationReminderTime))
        )

        if (oncePerDaySettingsState.medicationReminderTime  == 0L) {
            medicationTimeError.value = true
        }

        if (oncePerDaySettingsState.medicationDose  == 0) {
            medicationDoseError.value = true
        }

        if (isEveryFieldValid()){
            interactor.setMedicationReminder(quantityOfDays, medicationReminder)
        }
    }

    fun setMedicationReminderTime(time: Long) {
        oncePerDaySettingsState.medicationReminderTime = time
        medicationTimeError.value = false
    }

    fun setDose(dose: Int){
        oncePerDaySettingsState.medicationDose = dose
        medicationDoseError.value = false
    }
}