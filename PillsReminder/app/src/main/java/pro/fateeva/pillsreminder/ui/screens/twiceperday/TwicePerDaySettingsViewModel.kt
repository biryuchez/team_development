package pro.fateeva.pillsreminder.ui.screens.twiceperday

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationIntake
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationReminder
import pro.fateeva.pillsreminder.ui.SaveState

private const val FIRST_MEDICATION_INTAKE_INDEX = 0
private const val SECOND_MEDICATION_INTAKE_INDEX = 1

class TwicePerDaySettingsViewModel(
    private val handle: SavedStateHandle,
    private val interactor: MedicationInteractor
) : ViewModel() {

    private val liveData: MutableLiveData<TwicePerDaySettingsState> =
        handle.getLiveData("state", TwicePerDaySettingsState())
    private val firstMedicationTimeError = MutableLiveData(false)
    private val firstMedicationDoseError = MutableLiveData(false)
    private val secondMedicationTimeError = MutableLiveData(false)
    private val secondMedicationDoseError = MutableLiveData(false)

    private val twicePerDaySettingsState: TwicePerDaySettingsState
        get() = liveData.value ?: TwicePerDaySettingsState()

    val hasFirstMedicationTimeError: LiveData<Boolean>
        get() = firstMedicationTimeError

    val hasFirstMedicationDoseError: LiveData<Boolean>
        get() = firstMedicationDoseError

    val hasSecondMedicationTimeError: LiveData<Boolean>
        get() = secondMedicationTimeError

    val hasSecondMedicationDoseError: LiveData<Boolean>
        get() = secondMedicationDoseError

    private fun isEveryFieldValid() = twicePerDaySettingsState.let {
        it.firstMedicationDose > 0 && it.firstMedicationReminderTime > 0 && it.secondMedicationDose > 0 && it.secondMedicationReminderTime > 0
    }

    private val successErrorSaveLiveData: MutableLiveData<SaveState> =
        handle.getLiveData("saveState")

    val successErrorSaveState: LiveData<SaveState>
        get() = successErrorSaveLiveData

    fun setMedicationReminderTime(time: Long, medicationIntakeIndex : Int) {
        if (medicationIntakeIndex == FIRST_MEDICATION_INTAKE_INDEX){
            twicePerDaySettingsState.firstMedicationReminderTime = time
            firstMedicationTimeError.value = false
        } else if (medicationIntakeIndex == SECOND_MEDICATION_INTAKE_INDEX){
            twicePerDaySettingsState.secondMedicationReminderTime = time
            secondMedicationTimeError.value = false
        }
    }

    fun setDose(dose: Int, medicationIntakeIndex : Int){
        if (medicationIntakeIndex == FIRST_MEDICATION_INTAKE_INDEX){
            twicePerDaySettingsState.firstMedicationDose = dose
            firstMedicationDoseError.value = false
        } else if (medicationIntakeIndex == SECOND_MEDICATION_INTAKE_INDEX){
            twicePerDaySettingsState.secondMedicationDose = dose
            secondMedicationDoseError.value = false
        }
    }

    fun setMedicationReminder(quantityOfDays: Int, selectedDrug: DrugDomain) {
        val medicationReminder = MedicationReminder(
            selectedDrug.ID,
            selectedDrug.drugName,
            listOf(
                MedicationIntake(
                    twicePerDaySettingsState.firstMedicationDose,
                    twicePerDaySettingsState.firstMedicationReminderTime
                ),
                MedicationIntake(
                    twicePerDaySettingsState.secondMedicationDose,
                    twicePerDaySettingsState.secondMedicationReminderTime
                )
            )
        )

        if (twicePerDaySettingsState.firstMedicationReminderTime  == 0L) {
            firstMedicationTimeError.value = true
        }

        if (twicePerDaySettingsState.firstMedicationDose  == 0) {
            firstMedicationDoseError.value = true
        }

        if (twicePerDaySettingsState.secondMedicationReminderTime  == 0L) {
            secondMedicationTimeError.value = true
        }

        if (twicePerDaySettingsState.secondMedicationDose  == 0) {
            secondMedicationDoseError.value = true
        }

        if (isEveryFieldValid()) {
            interactor.saveMedicationReminder(quantityOfDays, medicationReminder)
            successErrorSaveLiveData.value = SaveState.SUCCESS
        }
    }
}