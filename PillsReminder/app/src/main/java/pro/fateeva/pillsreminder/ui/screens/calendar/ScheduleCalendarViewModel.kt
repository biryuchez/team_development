package pro.fateeva.pillsreminder.ui.screens.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pro.fateeva.pillsreminder.clean.data.local.entity.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.clean.domain.usecase.GetMedicationScheduleUsecase

class ScheduleCalendarViewModel(
    private val medicationScheduleRepository: GetMedicationScheduleUsecase,
) : ViewModel() {
    private val liveData = MutableLiveData<List<FakeMedicationScheduleEntity>>()

    fun getMedicationScheduleList(): LiveData<List<FakeMedicationScheduleEntity>> {
        liveData.postValue(medicationScheduleRepository.getMedicationSchedule())
        return liveData
    }
}