package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.EventFactory
import pro.fateeva.pillsreminder.ui.notification.NotificationEvent

class MainActivity : AppCompatActivity(), Notificator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                context = this@MainActivity)

        notificationEvent.setEvent()
    }
}