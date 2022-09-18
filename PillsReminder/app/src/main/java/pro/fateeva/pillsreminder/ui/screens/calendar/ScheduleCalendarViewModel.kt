package pro.fateeva.pillsreminder.ui.screens.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.data.local.FakeLocalRepository
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain

class ScheduleCalendarViewModel(
    private val fakeLocalRepository: FakeLocalRepository,
) : ViewModel() {
    private val liveData = MutableLiveData<List<MedicationScheduleItemDomain>>()

    fun getMedicationScheduleList(): LiveData<List<MedicationScheduleItemDomain>> {
        liveData.postValue(fakeLocalRepository.getMedicationSchedule())
        return liveData
    }
}