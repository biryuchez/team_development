package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener
import pro.fateeva.pillsreminder.ui.notification.notificationevents.NotificationEvent
import pro.fateeva.pillsreminder.ui.notification.notificationevents.NotificationEventFactory
import pro.fateeva.pillsreminder.ui.screens.pillsearching.SearchPillFragment

class MainActivity : AppCompatActivity(), NotificationHandler {

    override val alarmManager: AlarmManager by lazy {
        getSystemService(ALARM_SERVICE) as AlarmManager
    }

    override val actionListener: NotificationActionListener by lazy {
        MedicationActionListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, SearchPillFragment())
                .commit()
        }
    }

    override fun setNotification(medicineEvent: MedicationEventDomain) {
        val medicationEvent: NotificationEvent = NotificationEventFactory()
            .generateNotificationEvent(
                medicationEvent = medicineEvent,
                eventReminder = alarmManager,
                context = applicationContext)

        medicationEvent.setEvent()
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
}