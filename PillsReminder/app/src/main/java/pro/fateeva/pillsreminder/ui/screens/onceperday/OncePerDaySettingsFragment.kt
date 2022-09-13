package pro.fateeva.pillsreminder.ui.screens.onceperday

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.initTimePicker
import pro.fateeva.pillsreminder.ui.SaveState
import pro.fateeva.pillsreminder.ui.screens.BaseFragment

private const val TIME_PICKER_TAG = "TIME_PICKER"
private const val DRUG_ARG_KEY = "DRUG"
private const val DAYS_COUNT_ARG_KEY = "DAYS_COUNT"
private const val DEFAULT_DAYS_COUNT_VALUE = 1
private const val MEDICATION_REMINDER_ID_ARG_KEY = "MEDICATION_REMINDER_ID_ARG_KEY"

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

        fun newInstance(id: Int): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(
                    MEDICATION_REMINDER_ID_ARG_KEY to id
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val medicationReminderId = arguments?.getInt(MEDICATION_REMINDER_ID_ARG_KEY) ?: 0

        val selectedDrug = arguments?.getParcelable(DRUG_ARG_KEY) ?: DrugDomain()
        val medicationDaysCount = arguments?.getInt(DAYS_COUNT_ARG_KEY)
            ?: DEFAULT_DAYS_COUNT_VALUE

        if (medicationReminderId == 0) {
            viewModel.onViewCreated(selectedDrug.drugName)

            binding.planButton.setOnClickListener {
                viewModel.onCreateMedicationReminder(medicationDaysCount, selectedDrug)
            }
        } else {
            viewModel.onViewCreated(medicationReminderId)

            binding.planButton.setOnClickListener {
                viewModel.onEditMedicationReminder(medicationReminderId)
            }
        }

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

        viewModel.successErrorSaveState.observe(viewLifecycleOwner) {
            if (it == SaveState.SUCCESS) {
                navigator.navigateToPillListScreen()
            }
        }

        viewModel.state.observe(viewLifecycleOwner){
            binding.medicationTitleTextView.text = it.medicationName
            binding.oncePerDayTimePickerTextView.text = it.medicationReminderTime.toString()
            binding.dosePickerEditText.setText(it.medicationDose.toString())
        }
    }
}
