package pro.fateeva.pillsreminder.ui.mainactivity

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListFragment
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsFragment
import pro.fateeva.pillsreminder.ui.screens.frequency.FrequencyFragment
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsFragment
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillFragment

private const val NAVIGATION_BACKSTACK_NAME = "NAVIGATION_BACKSTACK"

class MainActivity : AppCompatActivity(), NotificationHandler, AppNavigation {

    override val alarmManager: AlarmManager by lazy {
        getSystemService(ALARM_SERVICE) as AlarmManager
    }

    override val actionListener: NotificationActionListener by lazy {
        MedicationActionListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, PillsListFragment())
                .commit()
        }
    }

    override fun onGetDrugAction(message: String) {
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_SHORT).show()
    }

    override fun onCancelDrugAction(message: String) {
        Toast.makeText(
            this@MainActivity,
            message,
            Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        actionListener.onNotificationAction(this, applicationContext, intent)
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

    override fun navigateToOncePerDayScreen(id: Int) {
        navigateToDestination(OncePerDaySettingsFragment.newInstance(id))
    }

    override fun navigateToTwicePerDayScreen(drugDomain: DrugDomain, daysCount: Int) {
        navigateToDestination(TwicePerDaySettingsFragment.newInstance(drugDomain, daysCount))
    }

    override fun navigateToDestination(destination: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_container, destination)
            .addToBackStack(NAVIGATION_BACKSTACK_NAME)
            .commit()
    }
}