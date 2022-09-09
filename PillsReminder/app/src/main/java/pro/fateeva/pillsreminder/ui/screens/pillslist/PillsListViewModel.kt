package pro.fateeva.pillsreminder.ui.screens.pillslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.MedicationInteractor
import pro.fateeva.pillsreminder.clean.MedicationReminder

class PillsListViewModel(
    private val interactor: MedicationInteractor
) : ViewModel() {

    private val liveData: MutableLiveData<List<MedicationReminder>> = MutableLiveData()

    val getLiveData: LiveData<List<MedicationReminder>>
        get() = liveData

    fun getMedicationReminders(){
        liveData.postValue(interactor.getMedicationReminders())
        interactor.planMedicationReminders()
    }
}