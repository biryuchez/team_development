package pro.fateeva.pillsreminder.ui.screens.onceperday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationIntake
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.ui.SaveState

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

    val state: LiveData<OncePerDaySettingsState>
        get() = liveData

    val hasMedicationTimeError: LiveData<Boolean>
        get() = medicationTimeError

    val hasMedicationDoseError: LiveData<Boolean>
        get() = medicationDoseError

    private fun isEveryFieldValid() = oncePerDaySettingsState.let {
        it.medicationDose > 0 && it.medicationReminderTime > 0
    }

    private val successErrorSaveLiveData: MutableLiveData<SaveState> =
        handle.getLiveData("saveState")

    val successErrorSaveState: LiveData<SaveState>
        get() = successErrorSaveLiveData

    fun onCreateMedicationReminder(quantityOfDays: Int, selectedDrug: DrugDomain) {
        val medicationReminder = MedicationReminder(
            selectedDrug.ID,
            selectedDrug.drugName,
            listOf(
                MedicationIntake(
                    oncePerDaySettingsState.medicationDose,
                    oncePerDaySettingsState.medicationReminderTime
                )
            )
        )

        if (oncePerDaySettingsState.medicationReminderTime == 0L) {
            medicationTimeError.value = true
        }

        if (oncePerDaySettingsState.medicationDose == 0) {
            medicationDoseError.value = true
        }

        if (isEveryFieldValid()) {
            interactor.saveMedicationReminder(quantityOfDays, medicationReminder)
            successErrorSaveLiveData.value = SaveState.SUCCESS
        }
    }

    fun onEditMedicationReminder(id : Int) {
        val medicationReminder = interactor.getMedicationReminder(id)
        val newMedicationReminder = MedicationReminder(
            medicationReminder.id,
            medicationReminder.medicationName,
            listOf(
                MedicationIntake(
                    oncePerDaySettingsState.medicationDose,
                    oncePerDaySettingsState.medicationReminderTime
                )
            ),
            medicationReminder.endDate
        )

        if (oncePerDaySettingsState.medicationReminderTime == 0L) {
            medicationTimeError.value = true
        }

        if (oncePerDaySettingsState.medicationDose == 0 || oncePerDaySettingsState.medicationDose.toString() == "") {
            medicationDoseError.value = true
        }

        if (isEveryFieldValid()) {
            interactor.editMedicationReminder(newMedicationReminder)
            successErrorSaveLiveData.value = SaveState.SUCCESS
        }
    }

    fun setMedicationReminderTime(time: Long) {
        oncePerDaySettingsState.medicationReminderTime = time
        medicationTimeError.value = false
    }

    fun setDose(dose: String) {
        if (dose == "" || dose.toInt() == 0){
            medicationDoseError.value = true
        } else {
            medicationDoseError.value = false
            oncePerDaySettingsState.medicationDose = dose.toInt()
        }
    }

    fun onViewCreated(medicationReminderId: Int) {
        val medicationReminder = interactor.getMedicationReminder(medicationReminderId)
        setMedicationReminderTime(medicationReminder.medicationIntakes[0].time)
        setDose(medicationReminder.medicationIntakes[0].dosage.toString())
        oncePerDaySettingsState.medicationName = medicationReminder.medicationName
    }

    fun onViewCreated(name: String) {
        oncePerDaySettingsState.medicationName = name
    }
}