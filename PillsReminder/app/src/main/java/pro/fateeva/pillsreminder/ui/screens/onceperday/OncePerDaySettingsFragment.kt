package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.mainactivity.NotificationHandler
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import java.util.*

private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1

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
        var firstMedicationTime = 0L

        binding.oncePerDayTitleTextView.text = selectedDrug.drugName

        binding.oncePerDayTimePickerTextView.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Время приема лекарства")
                .build()

            timePicker.addOnPositiveButtonClickListener {

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    set(Calendar.MINUTE, timePicker.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (timeInMillis < System.currentTimeMillis()) {
                        add(Calendar.DATE, 1)
                    }

                }

                firstMedicationTime = calendar.timeInMillis
                binding.oncePerDayTimePickerTextView.text =
                    TimeMapper().getTimeFromTimePicker(timePicker.hour, timePicker.minute)
            }

            timePicker.show(parentFragmentManager, "timePicker")
        }

        binding.planButton.setOnClickListener {
            if (binding.dosePickerTextView.text.isEmpty()) {
                Snackbar.make(binding.root, "Введите дозировку", Snackbar.LENGTH_SHORT).show()
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
            }
        }
    }
}