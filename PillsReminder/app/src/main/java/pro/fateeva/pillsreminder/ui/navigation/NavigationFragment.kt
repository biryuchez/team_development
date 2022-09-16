package pro.fateeva.pillsreminder.ui.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.FragmentNavigationBinding
import pro.fateeva.pillsreminder.ui.screens.BaseFragment
import pro.fateeva.pillsreminder.ui.screens.calendar.ScheduleCalendarFragment
import pro.fateeva.pillsreminder.ui.screens.frequency.FrequencyFragment
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsFragment
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillFragment
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListFragment
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsFragment

private const val NAVIGATION_BACKSTACK_NAME = "NAVIGATION_BACKSTACK"

class NavigationFragment :
    BaseFragment<FragmentNavigationBinding>(FragmentNavigationBinding::inflate), AppNavigation {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            navigateToMenuItem(PillsListFragment())
        }

        binding.bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_navigate_to_pills_list -> {
                    navigateToMenuItem(PillsListFragment(), menuItem.itemId)
                    true
                }

                R.id.action_navigate_to_schedule_calendar -> {
                    navigateToMenuItem(ScheduleCalendarFragment(), menuItem.itemId)
                    true
                }

                else -> true
            }
        }
    }

    override fun navigateToPillListScreen() {
        navigateToDestination(PillsListFragment())
    }

    override fun navigateToPillSearchingScreen() {
        navigateToDestination(SearchPillFragment())
    }

    override fun navigateToEventFrequencyScreen(drugDomain: DrugDomain) {
        navigateToDestination(FrequencyFragment.newInstance(drugDomain))
    }

    override fun navigateToOncePerDayScreen(drugDomain: DrugDomain, daysCount: Int) {
        navigateToDestination(OncePerDaySettingsFragment.newInstance(drugDomain, daysCount))
    }

    override fun navigateToTwicePerDayScreen(drugDomain: DrugDomain, daysCount: Int) {
        navigateToDestination(TwicePerDaySettingsFragment.newInstance(drugDomain, daysCount))
    }

    override fun navigateToDestination(destination: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .add(R.id.navigation_container, destination)
            .addToBackStack(NAVIGATION_BACKSTACK_NAME)
            .commit()
    }

    override fun navigateToMenuItem(destination: Fragment, clickedItemId: Int) {
        if (binding.bottomNavView.selectedItemId != clickedItemId) {
            parentFragmentManager
                .beginTransaction()
                .replace(binding.navigationContainer.id, destination)
                .commit()
        }
    }
}