package pro.fateeva.pillsreminder.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
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
private const val LAYOUT_DIMENSION_RATIO = "W,1:1"
private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"
private const val DATE_DELIMITER = '.'

class HistoryCalendarFragment :
    BaseFragment<FragmentHistoryCalendarBinding>(FragmentHistoryCalendarBinding::inflate) {

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rowsCount = 5
        val columnsCount = 3

        val dateList = mutableListOf<Date>()

        val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
        val currentDate = dateFormat.format(Calendar.getInstance().time)

        for (i in -7..7) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            val date = dateFormat.format(calendar.time)
            dateFormat.parse(date)?.let { dateList.add(it) }
        }

        repeat(rowsCount * columnsCount) { index ->
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
                        dateFormat.format(dateList[index]).substringAfter(DATE_DELIMITER)
                    historyCalendarItemCardDayTextView.text =
                        dateFormat.format(dateList[index]).substringBefore(DATE_DELIMITER)


                    if (dateList[index].before(dateFormat.parse(currentDate))) {
                        historyCalendarItemCardEventMarkerView.setBackgroundColor(
                            requireContext().getColor(android.R.color.holo_green_light)
                        )
                    }

                    if (dateList[index] == dateFormat.parse(currentDate)) {
                        historyCalendarItemCard.setCardBackgroundColor(
                            requireContext().getColor(R.color.selected_date)
                        )
                    }

                    if (index == 0) {
                        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        params.endToStart = historyCalendarItemView.id + 1
                        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    } else {
                        when (index % rowsCount) {
                            0 -> {
                                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                                params.endToStart = historyCalendarItemView.id + 1
                                params.topToBottom = historyCalendarItemView.id - rowsCount
                            }

                            in (1..rowsCount - 2) -> {
                                params.startToEnd = historyCalendarItemView.id - 1
                                params.endToStart = historyCalendarItemView.id + 1
                                params.topToTop = historyCalendarItemView.id - 1
                            }

                            (rowsCount - 1) -> {
                                params.startToEnd = historyCalendarItemView.id - 1
                                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                                params.topToTop = historyCalendarItemView.id - 1
                            }
                        }
                    }

                    calendarContainer.addView(historyCalendarItemView)
                }
            }
        }
    }
}