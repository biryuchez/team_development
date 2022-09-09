package pro.fateeva.pillsreminder.ui.screens.twiceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import pro.fateeva.pillsreminder.ui.SaveState
import pro.fateeva.pillsreminder.databinding.FragmentTwicePerDaySettingsBinding
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val FIRST_MEDICATION_INTAKE_INDEX = 0
private const val SECOND_MEDICATION_INTAKE_INDEX = 1

class TwicePerDaySettingsFragment : BaseFragment<FragmentTwicePerDaySettingsBinding>(
    FragmentTwicePerDaySettingsBinding::inflate
) {
    private val viewModel: TwicePerDaySettingsViewModel by stateViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hasFirstMedicationTimeError.observe(viewLifecycleOwner) {
            binding.firstTimeErrorTextView.isVisible = it
        }

        viewModel.hasFirstMedicationDoseError.observe(viewLifecycleOwner) {
            binding.firstDoseErrorTextView.isVisible = it
        }

        viewModel.hasSecondMedicationTimeError.observe(viewLifecycleOwner) {
            binding.secondTimeErrorTextView.isVisible = it
        }

        viewModel.hasSecondMedicationDoseError.observe(viewLifecycleOwner) {
            binding.secondDoseErrorTextView.isVisible = it
        }

        viewModel.successErrorSaveState.observe(viewLifecycleOwner){
            if (it == SaveState.SUCCESS){
                navigator.navigateToPillListScreen()
            }
        }

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()
        val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
            ?: DEFAULT_DAYS_COUNT_VALUE

        binding.medicationTitleTextView.text = selectedDrug.drugName

        binding.firstTimePickerTextView.initTimePicker(
            parentFragmentManager
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis, FIRST_MEDICATION_INTAKE_INDEX)
            binding.firstTimePickerTextView.setText(it.formatTime())
        }

        binding.firstDosePickerTextView.doAfterTextChanged {
            viewModel.setDose(it.toString().toInt(), FIRST_MEDICATION_INTAKE_INDEX)
        }

        binding.secondTimePickerTextView.initTimePicker(
            parentFragmentManager
        ) {
            viewModel.setMedicationReminderTime(it.timeInMillis, SECOND_MEDICATION_INTAKE_INDEX)
            binding.secondTimePickerTextView.setText(it.formatTime())
        }

        binding.secondDosePickerTextView.doAfterTextChanged {
            viewModel.setDose(it.toString().toInt(), SECOND_MEDICATION_INTAKE_INDEX)
        }

        binding.planButton.setOnClickListener {
            viewModel.setMedicationReminder(medicationDaysCount, selectedDrug)
        }
    }

    companion object {
        fun newInstance(drugDomain: DrugDomain, daysCount: Int): TwicePerDaySettingsFragment {
            return TwicePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    DRUG_ARG_KEY to drugDomain,
                    DAYS_COUNT_ARG_KEY to daysCount
                )
            }
        }
    }
}
