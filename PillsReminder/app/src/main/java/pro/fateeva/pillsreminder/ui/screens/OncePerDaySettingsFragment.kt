package pro.fateeva.pillsreminder.ui.screens

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.databinding.FragmentOncePerDaySettingsBinding

private const val DRUG_NAME_ARG_KEY = "DRUG_NAME"

class OncePerDaySettingsFragment :
    BaseFragment<FragmentOncePerDaySettingsBinding>(FragmentOncePerDaySettingsBinding::inflate) {

    companion object {
        fun newInstance(drugName: String): OncePerDaySettingsFragment {
            return OncePerDaySettingsFragment().apply {
                arguments = bundleOf(DRUG_NAME_ARG_KEY to drugName)
            }
        }
    }
}