package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.EventFactory
import pro.fateeva.pillsreminder.ui.notification.NotificationEvent

class MainActivity : AppCompatActivity(), Notificator {

    companion object {
        const val EVENT_INTENT_CATEGORY = "MEDICATION_CATEGORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onNewIntent(intent)

        // фейковое событие "Одноразовый прием лекарства" для теста работоспособности AlarmManager.
        // Выводит напоминание о приеме лекарства спустя 2 секунды после вызова onCreate()
        val fakeMedicineEvent = MedicationEventDomain.Single(
            ID = 100,
            drugName = "УСЛОВНОЕ ЛЕКАРСТВО",
            dosage = "Одна таблетка",
            firstMedicationTime = System.currentTimeMillis() + 2000
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
                context = applicationContext)

        notificationEvent.setEvent()
    }


    // В этом методе можно выполнять действие по клику на пришедшее уведомление
    override fun onNotificationClick() {
        Toast.makeText(
            applicationContext,
            "Пользователь нажал на уведомление",
            Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.categories?.let {
            if (intent.categories.contains(EVENT_INTENT_CATEGORY))
                onNotificationClick()
        }
    }
}