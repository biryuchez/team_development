package pro.fateeva.pillsreminder.ui.screens.calendar.builder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import pro.fateeva.pillsreminder.databinding.ItemScheduleEntityBinding
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
class ScheduleEventsItemsBuilder(private val dateFormat: SimpleDateFormat) {
    private val todayDate = dateFormat.format(Calendar.getInstance().time)

    fun showScheduleEvents(
        scheduleEventsContainer: LinearLayout,
        medicationScheduleList: List<MedicationScheduleItemDomain>,
        currentDate: String,
    ) {
        scheduleEventsContainer.apply {
            animate()
                .alpha(ALPHA_INVISIBLE)
                .withEndAction {
                    onAnimationEndAction(this, medicationScheduleList, currentDate)
                }
                .duration = ANIMATION_DURATION
        }
    }

    private fun onAnimationEndAction(
        scheduleEventsContainer: LinearLayout,
        medicationScheduleList: List<MedicationScheduleItemDomain>,
        currentDate: String,
    ) {
        scheduleEventsContainer.removeAllViews()

        val params = initLayoutParams()

        initCurrentDatePillsSet(medicationScheduleList, currentDate).forEach { pillName ->
            addPillNameView(scheduleEventsContainer, pillName, params)

            medicationScheduleList
                .filter { dateFormat.format(it.medicationTime) == currentDate }
                .filter { it.pillName == pillName }
                .sortedBy { it.medicationTime }
                .forEach { addScheduleView(scheduleEventsContainer, params, it, currentDate) }
        }

        scheduleEventsContainer.animate()
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
        medicationScheduleList: List<MedicationScheduleItemDomain>,
        currentDate: String,
    ): Set<String> {
        return mutableSetOf<String>().apply {
            medicationScheduleList
                .filter { dateFormat.format(it.medicationTime) == currentDate }
                .sortedBy { it.pillName }
                .forEach { this.add(it.pillName) }
        }
    }

    private fun addPillNameView(
        scheduleEventsContainer: LinearLayout,
        pillName: String,
        params: LinearLayout.LayoutParams,
    ) {
        scheduleEventsContainer.addView(TextView(scheduleEventsContainer.context).apply {
            textSize = PILL_NAME_TEXT_SIZE
            text = pillName
            layoutParams = params
        })
    }

    private fun addScheduleView(
        scheduleEventsContainer: LinearLayout,
        params: LinearLayout.LayoutParams,
        fakeMedicationScheduleEntity: MedicationScheduleItemDomain,
        currentDate: String,
    ) {
        with(scheduleEventsContainer.context) {
            val scheduleItemViewBinding =
                ItemScheduleEntityBinding.inflate(LayoutInflater.from(this))

            val timeFormat = SimpleDateFormat(TIME_FORMAT_PATTERN)

            with(scheduleItemViewBinding) {
                root.layoutParams = params

                scheduleEntityTimeTextView.text =
                    timeFormat.format(fakeMedicationScheduleEntity.medicationTime)

                dateFormat.parse(currentDate)?.let { date ->
                    if (date.before(dateFormat.parse(todayDate))) {
                        if (fakeMedicationScheduleEntity.medicationSuccessTime != -1L) {
                            scheduleEntityMedicationMarkerView
                                .setBackgroundColor(getColor(R.color.success_medication))
                            scheduleEntitySuccessFlagTextView.text = getString(
                                R.string.medication_success_message,
                                timeFormat.format(fakeMedicationScheduleEntity.medicationSuccessTime)
                            )
                        } else {
                            scheduleEntityMedicationMarkerView
                                .setBackgroundColor(getColor(R.color.failure_medication))
                        }
                    }
                }
                scheduleEventsContainer.addView(root)
            }
        }
    }
}