package pro.fateeva.pillsreminder.ui.screens

import androidx.core.os.bundleOf
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
}