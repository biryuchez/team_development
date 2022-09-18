package pro.fateeva.pillsreminder.ui.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentNavigationBinding
import pro.fateeva.pillsreminder.ui.mainactivity.AppNavigation
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import pro.fateeva.pillsreminder.ui.screens.calendar.ScheduleCalendarFragment
import pro.fateeva.pillsreminder.ui.screens.frequency.FrequencyFragment
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsFragment
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillFragment
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListFragment
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsFragment

class NavigationFragment :
    BaseFragment<FragmentNavigationBinding>(FragmentNavigationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            navigator.navigateToPillListScreen(true)
        }

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_navigate_to_pills_list -> {
                    if (binding.bottomNavView.selectedItemId != menuItem.itemId) {
                        navigator.navigateToPillListScreen(true)
                    }
                    true
                }

                R.id.action_navigate_to_schedule_calendar -> {
                    if (binding.bottomNavView.selectedItemId != menuItem.itemId) {
                        navigator.navigateToScheduleCalendarScreen(true)
                    }
                    true
                }

                else -> true
            }
        }
    }
}