package pro.fateeva.pillsreminder.ui.screens

import android.os.Bundle
import android.view.View
import pro.fateeva.pillsreminder.databinding.FragmentPillsListBinding

class PillsListFragment :
    BaseFragment<FragmentPillsListBinding>(FragmentPillsListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addMedicationEventFab.setOnClickListener {
            navigator.navigateToPillSearchingScreen()
        }
    }
}