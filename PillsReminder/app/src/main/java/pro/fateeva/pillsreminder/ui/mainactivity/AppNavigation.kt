package pro.fateeva.pillsreminder.ui.mainactivity

import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain

interface AppNavigation {
    fun navigateToPillListScreen()
    fun navigateToPillSearchingScreen()
    fun navigateToEventFrequencyScreen(drugDomain: DrugDomain)
    fun navigateToOncePerDayScreen(drugDomain: DrugDomain, daysCount: Int)
    fun navigateToOncePerDayScreen(id: Int)
    fun navigateToTwicePerDayScreen(drugDomain: DrugDomain, daysCount: Int)
    fun navigateToTwicePerDayScreen(id: Int)
    fun navigateToDestination(destination: Fragment)
}