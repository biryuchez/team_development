package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val TIME_PICKER_TAG = "TIME_PICKER"
private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1

class OncePerDaySettingsFragment :
    BaseFragment<FragmentOncePerDaySettingsBinding>(FragmentOncePerDaySettingsBinding::inflate) {

    private val viewModel: OncePerDaySettingsViewModel by stateViewModel()

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
        val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
            ?: DEFAULT_DAYS_COUNT_VALUE

        binding.medicationTitleTextView.text = selectedDrug.drugName

        binding.oncePerDayTimePickerTextView.initTimePicker(
            parentFragmentManager,
            TIME_PICKER_TAG
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis)
            binding.oncePerDayTimePickerTextView.text = it.formatTime()
        }

        binding.dosePickerEditText.doAfterTextChanged {
            viewModel.setDose(it.toString().toInt())
        }

        viewModel.hasMedicationTimeError.observe(viewLifecycleOwner) {
            binding.timeErrorTextView.isVisible = it
        }

        viewModel.hasMedicationDoseError.observe(viewLifecycleOwner) {
            binding.doseErrorTextView.isVisible = it
        }

        binding.planButton.setOnClickListener {
            viewModel.setMedicationReminder(medicationDaysCount, selectedDrug)
        }
    }
}
