package pro.fateeva.pillsreminder.ui.screens.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.ItemHistoryEntityBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Класс, отвечающий за отображение событий "прием лекарства" по клику на ячейку
 * календарной сетки
 */

private const val ANIMATION_DURATION = 150L
private const val ALPHA_VISIBLE = 1f
private const val ALPHA_INVISIBLE = 0f
private const val LAYOUT_MARGIN = 4
private const val TIME_FORMAT_PATTERN = "HH:mm"
private const val PILL_NAME_TEXT_SIZE = 20f

@SuppressLint("SimpleDateFormat")
class HistoryEventsItemsBuilder(private val dateFormat: SimpleDateFormat) {
    private val todayDate = dateFormat.format(Calendar.getInstance().time)

    fun showHistoryEvents(
        historyEventsContainer: LinearLayout,
        medicationHistoryList: List<FakeMedicationHistoryEntity>,
        currentDate: String,
    ) {
        historyEventsContainer.apply {
            animate()
                .alpha(ALPHA_INVISIBLE)
                .withEndAction {
                    onAnimationEndAction(this, medicationHistoryList, currentDate)
                }
                .duration = ANIMATION_DURATION
        }
    }

    private fun onAnimationEndAction(
        historyEventsContainer: LinearLayout,
        medicationHistoryList: List<FakeMedicationHistoryEntity>,
        currentDate: String,
    ) {
        historyEventsContainer.removeAllViews()

        val params = initLayoutParams()

        initCurrentDatePillsSet(medicationHistoryList, currentDate).forEach { pillName ->
            addPillNameView(historyEventsContainer, pillName, params)

            medicationHistoryList
                .filter { dateFormat.format(it.medicationTime) == currentDate }
                .filter { it.pillName == pillName }
                .sortedBy { it.medicationTime }
                .forEach { addHistoryView(historyEventsContainer, params, it, currentDate) }
        }

        historyEventsContainer.animate()
            .alpha(ALPHA_VISIBLE)
            .duration = ANIMATION_DURATION
    }

    private fun initLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(LAYOUT_MARGIN) }
    }

    private fun initCurrentDatePillsSet(
        medicationHistoryList: List<FakeMedicationHistoryEntity>,
        currentDate: String,
    ): Set<String> {
        return mutableSetOf<String>().apply {
            medicationHistoryList
                .filter { dateFormat.format(it.medicationTime) == currentDate }
                .sortedBy { it.pillName }
                .forEach { this.add(it.pillName) }
        }
    }

    private fun addPillNameView(
        historyEventsContainer: LinearLayout,
        pillName: String,
        params: LinearLayout.LayoutParams,
    ) {
        historyEventsContainer.addView(TextView(historyEventsContainer.context).apply {
            textSize = PILL_NAME_TEXT_SIZE
            text = pillName
            layoutParams = params
        })
    }

    private fun addHistoryView(
        historyEventsContainer: LinearLayout,
        params: LinearLayout.LayoutParams,
        fakeMedicationHistoryEntity: FakeMedicationHistoryEntity,
        currentDate: String,
    ) {
        val context = historyEventsContainer.context

        val historyItemViewBinding = ItemHistoryEntityBinding.inflate(LayoutInflater.from(context))

        val timeFormat = SimpleDateFormat(TIME_FORMAT_PATTERN)

        with(historyItemViewBinding) {
            root.layoutParams = params

            historyEntityTimeTextView.text =
                timeFormat.format(fakeMedicationHistoryEntity.medicationTime)

            dateFormat.parse(currentDate)?.let { date ->
                if (date.before(dateFormat.parse(todayDate))) {
                    if (fakeMedicationHistoryEntity.medicationSuccessTime != -1L) {
                        historyEntityMedicationMarkerView
                            .setBackgroundColor(context.getColor(R.color.success_medication))
                        historyEntitySuccessFlagTextView.text = context.getString(
                            R.string.medication_success_message,
                            timeFormat.format(fakeMedicationHistoryEntity.medicationSuccessTime)
                        )
                    } else {
                        historyEntityMedicationMarkerView.setBackgroundColor(context.getColor(R.color.failure_medication))
                    }
                }
            }
            historyEventsContainer.addView(root)
        }
    }
}