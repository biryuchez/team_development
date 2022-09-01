package pro.fateeva.pillsreminder.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.databinding.FragmentFrequencyBinding

private const val DRUG_NAME_ARG_KEY = "DRUG_NAME"

class FrequencyFragment :
    BaseFragment<FragmentFrequencyBinding>(FragmentFrequencyBinding::inflate) {

    companion object {
        fun newInstance(drugName: String): FrequencyFragment {
            return FrequencyFragment().apply {
                arguments = bundleOf(DRUG_NAME_ARG_KEY to drugName)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.twicePerDayRadioButton.isEnabled = false // временно отключены все варианты, кроме "Один раз в день"
        binding.whenNeededRadioButton.isEnabled = false // временно отключены все варианты, кроме "Один раз в день"

        val selectedDrugName = arguments?.getString(DRUG_NAME_ARG_KEY)
            ?: getString(R.string.arguments_error)

        binding.frequencyQuestionTextView.text =
            getString(R.string.medication_frequency_header, selectedDrugName)

        binding.frequencyNextScreenButton.setOnClickListener {
            when (binding.frequencyRadioGroup.checkedRadioButtonId) {
                binding.oncePerDayRadioButton.id -> {
                    navigator.navigateToOncePerDayScreen(selectedDrugName)
                }
            }
        }
    }
}