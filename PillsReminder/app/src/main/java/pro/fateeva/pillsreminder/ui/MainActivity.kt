package pro.fateeva.pillsreminder.ui

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.EventFactory
import pro.fateeva.pillsreminder.ui.notification.MedicationEventReceiver
import pro.fateeva.pillsreminder.ui.notification.NotificationEvent
import java.util.*


class MainActivity : AppCompatActivity(), Notificator {

    companion object {
        const val EVENT_INTENT_CATEGORY = "MEDICATION_CATEGORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        val fakeMedicineEvent = MedicationEventDomain.Repeating(
            ID = 100,
            pillName = "УСЛОВНОЕ ЛЕКАРСТВО",
            dosage = "Одна таблетка",
            firstMedicationTime = System.currentTimeMillis() + 2000,
            medicationInterval = AlarmManager.INTERVAL_DAY
        )
        setNotification(fakeMedicineEvent)
    }

    override val manager: AlarmManager by lazy {
        getSystemService(ALARM_SERVICE) as AlarmManager
    }

    override fun setNotification(medicineEvent: MedicationEventDomain) {
        val notificationEvent: NotificationEvent = EventFactory.NotificationEventFactory()
            .generateNotificationEvent(
                medicationEvent = medicineEvent,
                eventReminder = manager,
                context = applicationContext
            )

        notificationEvent.setEvent()
    }

    // В этом методе можно выполнять действие по клику на пришедшее уведомление
    override fun onNotificationClick() {
        Toast.makeText(
            applicationContext,
            "Пользователь нажал на уведомление",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.categories?.let {
            if (intent.categories.contains(EVENT_INTENT_CATEGORY))
                onNotificationClick()
        }
    }
}