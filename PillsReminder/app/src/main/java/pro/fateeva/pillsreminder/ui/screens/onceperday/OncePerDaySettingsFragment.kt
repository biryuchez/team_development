package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.mainactivity.NotificationHandler
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import java.util.*

private const val TIME_PICKER_TAG = "TIME_PICKER"
private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val DEFAULT_MEDICATION_TIME_VALUE = 0L
private const val DEFAULT_TIME_PICKER_VALUE = 0
private const val TOMORROW_DATE_OFFSET = 1

class OncePerDaySettingsFragment :
    BaseFragment<FragmentOncePerDaySettingsBinding>(FragmentOncePerDaySettingsBinding::inflate) {

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()
        var firstMedicationTime = DEFAULT_MEDICATION_TIME_VALUE

        binding.oncePerDayTitleTextView.text = selectedDrug.drugName

        binding.oncePerDayTimePickerTextView.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(DEFAULT_TIME_PICKER_VALUE)
                .setMinute(DEFAULT_TIME_PICKER_VALUE)
                .setTitleText(getString(R.string.time_picker_title))
                .build()

            timePicker.addOnPositiveButtonClickListener {

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    set(Calendar.MINUTE, timePicker.minute)
                    set(Calendar.SECOND, DEFAULT_TIME_PICKER_VALUE)
                    set(Calendar.MILLISECOND, DEFAULT_TIME_PICKER_VALUE)
                    if (timeInMillis < System.currentTimeMillis()) {
                        add(Calendar.DATE, TOMORROW_DATE_OFFSET)
                    }
                }

                firstMedicationTime = calendar.timeInMillis
                binding.oncePerDayTimePickerTextView.text =
                    TimeMapper().getTimeFromTimePicker(timePicker.hour, timePicker.minute)
            }

            timePicker.show(parentFragmentManager, TIME_PICKER_TAG)
        }

        binding.planButton.setOnClickListener {
            if (firstMedicationTime == DEFAULT_MEDICATION_TIME_VALUE) {
                Snackbar.make(binding.root,
                    getString(R.string.set_time_warning),
                    Snackbar.LENGTH_SHORT).show()
            } else if (binding.dosePickerTextView.text.isEmpty()) {
                Snackbar.make(binding.root,
                    getString(R.string.set_dosage_warning),
                    Snackbar.LENGTH_SHORT).show()
            } else {
                val dosage = binding.dosePickerTextView.text.toString().toInt()

                val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
                    ?: DEFAULT_DAYS_COUNT_VALUE

                (requireActivity() as NotificationHandler).setNotification(
                    MedicationEventDomain.MedicationEvent(
                        selectedDrug.ID,
                        selectedDrug.drugName,
                        dosage,
                        firstMedicationTime,
                        medicationDaysCount
                    )
                )

                Snackbar.make(
                    binding.root,
                    getString(R.string.notification_is_setted),
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}