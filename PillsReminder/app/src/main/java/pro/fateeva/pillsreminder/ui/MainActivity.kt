package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener
import pro.fateeva.pillsreminder.ui.notification.medicationevents.EventFactory
import pro.fateeva.pillsreminder.ui.notification.actionlistener.NotificationActionListener
import pro.fateeva.pillsreminder.ui.notification.medicationevents.NotificationEvent
import pro.fateeva.pillsreminder.ui.notification.medicationevents.NotificationEventFactory

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

        findViewById<Button>(R.id.button).setOnClickListener {
            val fakeMedicineEvent = MedicationEventDomain.Single(
                ID = 100,
                drugName = findViewById<EditText>(R.id.edit_text).text.toString(),
                dosage = "Одна таблетка",
                firstMedicationTime = System.currentTimeMillis() + 1000
            )

            // фейковое событие "Одноразовый прием лекарства" для теста работоспособности AlarmManager.
            // Выводит напоминание о приеме лекарства через секунду после клика по кнопке
            setNotification(fakeMedicineEvent)
        }
    }

    override fun setNotification(medicineEvent: MedicationEventDomain) {
        val notificationEvent: NotificationEvent = NotificationEventFactory()
            .generateNotificationEvent(
                medicationEvent = medicineEvent,
                eventReminder = alarmManager,
                context = applicationContext)

        notificationEvent.setEvent()
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