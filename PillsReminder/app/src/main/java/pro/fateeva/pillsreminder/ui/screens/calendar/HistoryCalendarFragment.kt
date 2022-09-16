package pro.fateeva.pillsreminder.ui.screens.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import pro.fateeva.pillsreminder.databinding.FragmentHistoryCalendarBinding
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import java.text.SimpleDateFormat

private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"

@SuppressLint("SimpleDateFormat")
class HistoryCalendarFragment :
    BaseFragment<FragmentHistoryCalendarBinding>(FragmentHistoryCalendarBinding::inflate) {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationHistoryList = FakeMedicationListRepository(FakeLocalDatasource())
            .getMedicationList()

        renderCalendarGrid(medicationHistoryList)
    }

    private fun renderCalendarGrid(medicationHistoryList: List<FakeMedicationHistoryEntity>) {
        CalendarGridBuilder(dateFormat).buildCalendarGrid(
            binding.calendarContainer,
            binding.calendarDaysHeader.root,
            medicationHistoryList
        ) { currentDate -> renderMedicationHistory(medicationHistoryList, currentDate) }
    }

    private fun renderMedicationHistory(
        medicationHistoryList: List<FakeMedicationHistoryEntity>,
        currentDate: String,
    ) {
        HistoryEventsItemsBuilder(dateFormat).showHistoryEvents(
            binding.historyEventsContainer,
            medicationHistoryList,
            currentDate
        )
    }
}