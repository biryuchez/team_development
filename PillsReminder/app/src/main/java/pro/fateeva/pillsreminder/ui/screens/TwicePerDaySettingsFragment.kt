package pro.fateeva.pillsreminder.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.MedicationReminder
import pro.fateeva.pillsreminder.databinding.FragmentTwicePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import java.util.*

private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1

class TwicePerDaySettingsFragment : BaseFragment<FragmentTwicePerDaySettingsBinding>(
    FragmentTwicePerDaySettingsBinding::inflate)  {

    private val viewModel: TwicePerDaySettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()

        val firstMedicationReminderTime = Calendar.getInstance()
        firstMedicationReminderTime.set(Calendar.HOUR_OF_DAY, 8)
        firstMedicationReminderTime.set(Calendar.MINUTE, 0)
        firstMedicationReminderTime.set(Calendar.SECOND, 0)
        firstMedicationReminderTime.set(Calendar.MILLISECOND, 0)

        val secondMedicationReminderTime = Calendar.getInstance()
        secondMedicationReminderTime.set(Calendar.HOUR_OF_DAY, 20)
        secondMedicationReminderTime.set(Calendar.MINUTE, 0)
        secondMedicationReminderTime.set(Calendar.SECOND, 0)
        secondMedicationReminderTime.set(Calendar.MILLISECOND, 0)

        var remindersTime = mutableListOf<Long>(firstMedicationReminderTime.timeInMillis, secondMedicationReminderTime.timeInMillis)

        binding.medicationTitleTextView.text = selectedDrug.drugName

        binding.firstTimePickerTextView.initTimePicker(
            parentFragmentManager
        ) {
            remindersTime.set(0, it.timeInMillis)
            binding.firstTimePickerTextView.setText(it.formatTime())
        }

        binding.secondTimePickerTextView.initTimePicker(
            parentFragmentManager
        ) {
            remindersTime.set(1, it.timeInMillis)
            binding.secondTimePickerTextView.setText(it.formatTime())
        }

        val dosage = binding.firstDosePickerTextView.text.toString().toIntOrNull()

        val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
            ?: DEFAULT_DAYS_COUNT_VALUE

        val medicationReminder = MedicationReminder(
            selectedDrug.ID,
            selectedDrug.drugName,
            dosage ?: 0,
            remindersTime
        )

        viewModel.setMedicationReminder(medicationDaysCount, medicationReminder)

        Snackbar.make(
            binding.root,
            getString(R.string.notification_is_setted),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): TwicePerDaySettingsFragment {
            return TwicePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount)
            }
        }
    }
}