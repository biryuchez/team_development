package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.MedicationReminder
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val TIME_PICKER_TAG = "TIME_PICKER"
private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val DEFAULT_MEDICATION_TIME_VALUE = 0L

class OncePerDaySettingsFragment :
    BaseFragment<FragmentOncePerDaySettingsBinding>(FragmentOncePerDaySettingsBinding::inflate) {

    private val viewModel: OncePerDaySettingsViewModel by viewModel()

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()
        var firstMedicationTime = DEFAULT_MEDICATION_TIME_VALUE

        binding.medicationTitleTextView.text = selectedDrug.drugName

        binding.oncePerDayTimePickerTextView.initTimePicker(
            parentFragmentManager,
            TIME_PICKER_TAG
        ) {
            firstMedicationTime = it.timeInMillis
            binding.oncePerDayTimePickerTextView.text = it.formatTime()
        }

        binding.planButton.setOnClickListener {
            if (firstMedicationTime == DEFAULT_MEDICATION_TIME_VALUE) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.set_time_warning),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (binding.dosePickerTextView.text.isEmpty()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.set_dosage_warning),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val dosage = binding.dosePickerTextView.text.toString().toInt()

                val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
                    ?: DEFAULT_DAYS_COUNT_VALUE

                val medicationReminder = MedicationReminder(
                    selectedDrug.ID,
                    selectedDrug.drugName,
                    dosage,
                    listOf(firstMedicationTime)
                )

                viewModel.setMedicationReminder(medicationDaysCount, medicationReminder)

                Snackbar.make(
                    binding.root,
                    getString(R.string.notification_is_setted),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}