package pro.fateeva.pillsreminder.ui.screens.pillslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.MedicationInteractor
import pro.fateeva.pillsreminder.clean.MedicationReminder
import pro.fateeva.pillsreminder.databinding.FragmentPillsListBinding
import pro.fateeva.pillsreminder.databinding.ItemPillBinding
import pro.fateeva.pillsreminder.extensions.formatTime
import pro.fateeva.pillsreminder.extensions.toCalendar
import pro.fateeva.pillsreminder.ui.RecyclerAdapter
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsViewModel

class PillsListFragment :
    BaseFragment<FragmentPillsListBinding>(FragmentPillsListBinding::inflate) {

    private val viewModel: PillsListViewModel by stateViewModel()

    val adapter = RecyclerAdapter<MedicationReminder>(
        emptyList(),
        R.layout.item_pill
    ) { medicationreminder, _ ->
        ItemPillBinding.bind(this).apply {
            medicationTitleTextView.text = medicationreminder.medicationName
            scheduleTextView.text =
                medicationreminder.medicationIntakes.map { it.time.toCalendar().formatTime() }
                    .joinToString(", ")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addMedicationEventFab.setOnClickListener {
            navigator.navigateToPillSearchingScreen()
        }

        binding.recyclerView.adapter = adapter

        viewModel.getLiveData.observe(viewLifecycleOwner) {
            renderMedicationReminders(it)
        }

        viewModel.getMedicationReminders()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMedicationReminders()
    }

    private fun renderMedicationReminders(medicationReminders: List<MedicationReminder>) {
        binding.emptyPillsListLottie.isVisible = medicationReminders.isEmpty()
        adapter.itemList = medicationReminders
    }
}