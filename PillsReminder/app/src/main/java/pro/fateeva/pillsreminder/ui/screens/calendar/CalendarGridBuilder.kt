package pro.fateeva.pillsreminder.ui.screens.calendar

import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.ItemHistoryCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_COLUMNS_COUNT = 7
private const val DEFAULT_LAYOUT_SIZE = 0
private const val LAYOUT_DIMENSION_RATIO = "W,1:1"
private const val DEFAULT_DATES_COUNT = 30
private const val DATE_DELIMITER = '.'

class CalendarGridBuilder(private val dateFormat: SimpleDateFormat) {

    private val dateList = DateListFactory().getDatesList(DEFAULT_DATES_COUNT)

    private val todayDate = dateFormat.format(Calendar.getInstance().time)

    private val firstDateInList = Calendar.getInstance().apply {
        timeInMillis = dateList[0]
    }

    private val offset = if (firstDateInList.get(Calendar.DAY_OF_WEEK) == 1) {
        firstDateInList.get(Calendar.DAY_OF_WEEK) + 5
    } else {
        firstDateInList.get(Calendar.DAY_OF_WEEK) - 2
    }

    fun buildCalendarGrid(
        calendarContainer: ConstraintLayout,
        medicationEventList: List<FakeMedicationHistoryEntity>,
        block: (currentDate: String) -> Unit
    ) {
        val context = calendarContainer.context
        repeat(DEFAULT_DATES_COUNT + offset) { index ->
            var historyCalendarItemView: View

            val historyCalendarItemBinding = ItemHistoryCalendarBinding.inflate(
                LayoutInflater.from(calendarContainer.context)
            )

            val params = ConstraintLayout.LayoutParams(DEFAULT_LAYOUT_SIZE, DEFAULT_LAYOUT_SIZE)
            params.dimensionRatio = LAYOUT_DIMENSION_RATIO

            with(historyCalendarItemBinding) {
                when (index) {
                    in 0 until offset -> {
                        historyCalendarItemView = View(context)
                    }
                    else -> {
                        historyCalendarItemView = historyCalendarItemBinding.root
                        val currentDate = dateFormat.format(dateList[index - offset])
                        historyCalendarItemCard.setOnClickListener {
                            calendarContainer.children.iterator()
                                .forEachRemaining { parentView ->
                                    if (parentView.id !in 1..offset) {
                                        parentView
                                            .findViewById<MaterialCardView>(
                                                R.id.history_calendar_item_card
                                            )
                                            .setCardBackgroundColor(
                                                context
                                                    .getColor(R.color.calendar_item)
                                            )
                                    }
                                }
                            historyCalendarItemCard.setCardBackgroundColor(
                                context.getColor(R.color.selected_date)
                            )

                            block(currentDate)
                        }

                        historyCalendarItemCardMmYyTextView.text =
                            currentDate.substringAfter(DATE_DELIMITER)
                        historyCalendarItemCardDayTextView.text =
                            currentDate.substringBefore(DATE_DELIMITER)

                        dateFormat.parse(currentDate)?.let { date ->
                            var isMedicationSuccess = true
                            medicationEventList
                                .filter { dateFormat.format(it.medicationTime) == currentDate }
                                .forEach {
                                    historyCalendarItemCardEventMarkerView.isVisible = true
                                    if (date.before(dateFormat.parse(todayDate))) {
                                        if (it.medicationSuccessTime == -1L) {
                                            isMedicationSuccess = false
                                        }
                                        historyCalendarItemCardEventMarkerView.apply {
                                            setBackgroundColor(context
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
                                context.getColor(R.color.selected_date)
                            )
                            block(currentDate)
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