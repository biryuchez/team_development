package pro.fateeva.pillsreminder.ui.mainactivity

import androidx.fragment.app.Fragment

interface AppNavigation {
    fun navigateToPillSearchingScreen()
    fun navigateToEventFrequencyScreen(drugName: String)
    fun navigateToOncePerDayScreen(drugName: String)
    fun navigateToDestination(destination: Fragment)
}