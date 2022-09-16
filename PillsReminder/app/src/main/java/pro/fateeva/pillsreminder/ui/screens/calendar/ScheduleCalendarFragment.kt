package pro.fateeva.pillsreminder.ui.screens.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.clean.data.local.entity.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.databinding.FragmentScheduleCalendarBinding
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import pro.fateeva.pillsreminder.ui.screens.calendar.builder.CalendarGridBuilder
import pro.fateeva.pillsreminder.ui.screens.calendar.builder.ScheduleEventsItemsBuilder
import java.text.SimpleDateFormat

private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"

@SuppressLint("SimpleDateFormat")
class ScheduleCalendarFragment :
    BaseFragment<FragmentScheduleCalendarBinding>(FragmentScheduleCalendarBinding::inflate) {

    private val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)

    private val viewModel: ScheduleCalendarViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMedicationScheduleList().observe(viewLifecycleOwner) {
            renderCalendarGrid(it)
        }
    }

    private fun renderCalendarGrid(medicationScheduleList: List<FakeMedicationScheduleEntity>) {
        CalendarGridBuilder(dateFormat).buildCalendarGrid(
            binding.calendarContainer,
            binding.calendarDaysHeader.root,
            medicationScheduleList
        ) { currentDate -> renderMedicationSchedule(medicationScheduleList, currentDate) }
    }

    private fun renderMedicationSchedule(
        medicationScheduleList: List<FakeMedicationScheduleEntity>,
        currentDate: String,
    ) {
        ScheduleEventsItemsBuilder(dateFormat).showScheduleEvents(
            binding.scheduleEventsContainer,
            medicationScheduleList,
            currentDate
        )
    }
}