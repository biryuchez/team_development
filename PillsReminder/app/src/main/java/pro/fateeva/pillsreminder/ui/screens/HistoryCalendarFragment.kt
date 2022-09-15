package pro.fateeva.pillsreminder.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.FragmentHistoryCalendarBinding
import pro.fateeva.pillsreminder.databinding.ItemHistoryCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_LAYOUT_SIZE = 0
private const val DEFAULT_COLUMNS_COUNT = 7
private const val DEFAULT_DATES_COUNT = 30
private const val LAYOUT_DIMENSION_RATIO = "W,1:1"
private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"
private const val DATE_DELIMITER = '.'

@SuppressLint("SimpleDateFormat")
class HistoryCalendarFragment :
    BaseFragment<FragmentHistoryCalendarBinding>(FragmentHistoryCalendarBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationEventList = mutableListOf<FakeMedicationHistoryEntity>()

        val dateList = mutableListOf<Long>()

        val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)

        val todayDate = dateFormat.format(Calendar.getInstance().time)

        for (i in -DEFAULT_DATES_COUNT / 2..(-DEFAULT_DATES_COUNT / 2) + DEFAULT_DATES_COUNT) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            dateList.add(calendar.timeInMillis)
            calendar.set(Calendar.HOUR_OF_DAY, 10)
            calendar.set(Calendar.MINUTE, 0)
            if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
                medicationEventList.add(FakeMedicationHistoryEntity(
                    medicationTime = calendar.timeInMillis,
                    isMedicationSuccess = i % 3 != 0))
                calendar.set(Calendar.HOUR_OF_DAY, 20)
                medicationEventList.add(FakeMedicationHistoryEntity(
                    medicationTime = calendar.timeInMillis,
                    isMedicationSuccess = true))
            }
        }

        Calendar.FRIDAY

        val firstDateInList = Calendar.getInstance().apply {
            timeInMillis = dateList[0]
        }

        val offset = if (firstDateInList.get(Calendar.DAY_OF_WEEK) == 1) {
            firstDateInList.get(Calendar.DAY_OF_WEEK) + 6
        } else {
            firstDateInList.get(Calendar.DAY_OF_WEEK) - 2
        }

        repeat(DEFAULT_DATES_COUNT + offset) { index ->
            var historyCalendarItemView: View

            val historyCalendarItemBinding =
                ItemHistoryCalendarBinding.inflate(LayoutInflater.from(requireContext()))

            val params =
                ConstraintLayout.LayoutParams(DEFAULT_LAYOUT_SIZE, DEFAULT_LAYOUT_SIZE)
            params.dimensionRatio = LAYOUT_DIMENSION_RATIO

            with(binding) {
                with(historyCalendarItemBinding) {
                    when (index) {
                        in 0 until offset -> {
                            historyCalendarItemView = View(requireContext())
                        }
                        else -> {
                            historyCalendarItemView = historyCalendarItemBinding.root
                            val currentDate = dateFormat.format(dateList[index - offset])
                            historyCalendarItemCard.setOnClickListener {
                                calendarContainer.children.iterator()
                                    .forEachRemaining { parentView ->
                                        if (parentView.id !in 1..offset) {
                                            parentView
                                                .findViewById<MaterialCardView>(R.id.history_calendar_item_card)
                                                .setCardBackgroundColor(requireContext()
                                                    .getColor(R.color.calendar_item))
                                        }
                                    }
                                historyCalendarItemCard.setCardBackgroundColor(
                                    requireContext().getColor(R.color.selected_date)
                                )

                                showHistoryEvents(medicationEventList
                                    .filter { dateFormat.format(it.medicationTime) == currentDate })
                            }

                            historyCalendarItemCardMmYyTextView.text =
                                currentDate.substringAfter(DATE_DELIMITER)
                            historyCalendarItemCardDayTextView.text =
                                currentDate.substringBefore(DATE_DELIMITER)

                            dateFormat.parse(currentDate)?.let { date ->
                                var isMedicationSuccess = true
                                medicationEventList
                                    .filter { dateFormat.format(it.medicationTime) == currentDate }
                                    .map {
                                        historyCalendarItemCardEventMarkerView.isVisible = true
                                        if (date.before(dateFormat.parse(todayDate))) {
                                            if (!it.isMedicationSuccess) {
                                                isMedicationSuccess = false
                                            }
                                            historyCalendarItemCardEventMarkerView.apply {
                                                setBackgroundColor(requireContext()
                                                    .getColor(if (isMedicationSuccess) {
                                                        R.color.success_medication
                                                    } else {
                                                        R.color.failure_medication
                                                    }))
                                            }
                                        }
                                    }
                            }

                            if (dateFormat.parse(currentDate) == dateFormat.parse(todayDate)) {
                                historyCalendarItemCard.setCardBackgroundColor(
                                    requireContext().getColor(R.color.selected_date)
                                )
                            }
                        }
                    }
                }

                historyCalendarItemView.apply {
                    id = index + 1
                    layoutParams = params
                }

                when (index) {
                    0 -> {
                        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        params.endToStart = historyCalendarItemView.id + 1
                        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                    in 1 until DEFAULT_COLUMNS_COUNT - 1 -> {
                        params.startToEnd = historyCalendarItemView.id - 1
                        params.endToStart = historyCalendarItemView.id + 1
                        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                    DEFAULT_COLUMNS_COUNT - 1 -> {
                        params.startToEnd = historyCalendarItemView.id - 1
                        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    }
                    else -> {
                        params.startToStart = historyCalendarItemView.id - DEFAULT_COLUMNS_COUNT
                        params.endToEnd = historyCalendarItemView.id - DEFAULT_COLUMNS_COUNT
                        params.topToBottom = historyCalendarItemView.id - DEFAULT_COLUMNS_COUNT
                    }
                }


                calendarContainer.addView(historyCalendarItemView)
            }
        }
    }

    private fun showHistoryEvents(medicationHistoryList: List<FakeMedicationHistoryEntity>) {
        binding.historyEventsContainer.removeAllViews()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss")
        if (medicationHistoryList.isNotEmpty()) {
            medicationHistoryList.map {
                val dateView = TextView(requireContext()).apply {
                    text = dateFormat.format(it.medicationTime)
                }

                val isMedicationSuccessView = TextView(requireContext()).apply {
                    text = it.isMedicationSuccess.toString()
                }
                binding.historyEventsContainer.addView(dateView)
                binding.historyEventsContainer.addView(isMedicationSuccessView)
            }
        }
    }
}

data class FakeMedicationHistoryEntity(
    val id: Int = -1,
    val medicationTime: Long,
    val pillName: String = "Анальгин",
    val isMedicationSuccess: Boolean,
)