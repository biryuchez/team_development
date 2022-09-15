package pro.fateeva.pillsreminder.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.FragmentHistoryCalendarBinding
import pro.fateeva.pillsreminder.databinding.ItemHistoryCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_LAYOUT_SIZE = 0
private const val DEFAULT_COLUMNS_COUNT = 7
private const val DEFAULT_DATES_COUNT = 31
private const val LAYOUT_DIMENSION_RATIO = "W,1:1"
private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"
private const val DATE_DELIMITER = '.'

class HistoryCalendarFragment :
    BaseFragment<FragmentHistoryCalendarBinding>(FragmentHistoryCalendarBinding::inflate) {

    @SuppressLint("SimpleDateFormat")
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
            medicationEventList.add(FakeMedicationHistoryEntity(
                medicationTime = calendar.timeInMillis,
                isMedicationSuccess = i % 2 == 0))
            calendar.set(Calendar.HOUR_OF_DAY, 20)
            medicationEventList.add(FakeMedicationHistoryEntity(medicationTime = calendar.timeInMillis,
                isMedicationSuccess = i % 2 == 0))
        }

        repeat(DEFAULT_DATES_COUNT) { index ->
            val currentDate = dateFormat.format(dateList[index])
            val historyCalendarItemBinding =
                ItemHistoryCalendarBinding.inflate(LayoutInflater.from(requireContext()))

            val historyCalendarItemView = historyCalendarItemBinding.root.apply {
                id = index + 1
            }

            with(binding) {
                with(historyCalendarItemBinding) {
                    historyCalendarItemCard.setOnClickListener {
                        calendarContainer.children.iterator().forEachRemaining { parentView ->
                            parentView
                                .findViewById<MaterialCardView>(R.id.history_calendar_item_card)
                                .setCardBackgroundColor(requireContext()
                                    .getColor(R.color.calendar_item))
                        }
                        historyCalendarItemCard.setCardBackgroundColor(
                            requireContext().getColor(R.color.selected_date)
                        )
                    }

                    val params =
                        ConstraintLayout.LayoutParams(DEFAULT_LAYOUT_SIZE, DEFAULT_LAYOUT_SIZE)
                    params.dimensionRatio = LAYOUT_DIMENSION_RATIO
                    historyCalendarItemView.layoutParams = params

                    historyCalendarItemCardMmYyTextView.text =
                        currentDate.substringAfter(DATE_DELIMITER)
                    historyCalendarItemCardDayTextView.text =
                        currentDate.substringBefore(DATE_DELIMITER)

                    dateFormat.parse(currentDate)?.let { date ->
                        if (date.before(dateFormat.parse(todayDate))) {
                            medicationEventList
                                .filter { dateFormat.format(it.medicationTime) == currentDate }
                                .map {
                                    historyCalendarItemCardEventMarkerView.setBackgroundColor(
                                        requireContext().getColor(
                                            R.color.success_medication))
                                    if (!it.isMedicationSuccess) {
                                        historyCalendarItemCardEventMarkerView.setBackgroundColor(
                                            requireContext().getColor(
                                                R.color.failure_medication))
                                    }
                                }
                        }
                    }


                    if (dateFormat.parse(currentDate) == dateFormat.parse(todayDate)) {
                        historyCalendarItemCard.setCardBackgroundColor(
                            requireContext().getColor(R.color.selected_date)
                        )
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
                }

                calendarContainer.addView(historyCalendarItemView)
            }
        }
    }
}

data class FakeMedicationHistoryEntity(
    val id: Int = -1,
    val medicationTime: Long,
    val pillName: String = "Анальгин",
    val isMedicationSuccess: Boolean = false,
)