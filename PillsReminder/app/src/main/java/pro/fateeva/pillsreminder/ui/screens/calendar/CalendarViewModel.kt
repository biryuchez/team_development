package pro.fateeva.pillsreminder.ui.screens.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel(
    private val medicationHistoryRepository: GetMedicationHistoryUsecase,
) : ViewModel() {
    private val liveData = MutableLiveData<List<FakeMedicationHistoryEntity>>()

    fun getData(): LiveData<List<FakeMedicationHistoryEntity>> {
        liveData.postValue(medicationHistoryRepository.getMedicationHistory())
        return liveData
    }
}