package pro.fateeva.pillsreminder.ui.mainactivity

import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.domain.entity.DrugDomain

interface AppNavigation {
    fun navigateToPillSearchingScreen()
    fun navigateToEventFrequencyScreen(drugDomain: DrugDomain)
    fun navigateToOncePerDayScreen(drugDomain: DrugDomain, daysCount: Int)
    fun navigateToDestination(destination: Fragment)
}