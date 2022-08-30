package pro.fateeva.pillsreminder.ui

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.EventFactory
import pro.fateeva.pillsreminder.ui.notification.NotificationEvent

class MainActivity : AppCompatActivity(), Notificator {

    companion object {
        const val MEDICATION_EVENT_INTENT_CATEGORY = "MEDICATION_EVENT_INTENT_CATEGORY"
        const val GET_DRUG_ACTION_EXTRA_KEY = "GET_DRUG_ACTION"
        const val CANCEL_DRUG_ACTION_EXTRA_KEY = "CANCEL_DRUG_ACTION_EXTRA_KEY"
        const val NOTIFICATION_ID_EXTRA_KEY = "NOTIFICATION_ID"
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
                firstMedicationTime = System.currentTimeMillis() + 2000
            )

            // фейковое событие "Одноразовый прием лекарства" для теста работоспособности AlarmManager.
            // Выводит напоминание о приеме лекарства спустя 2 секунды после клика по кнопке
            setNotification(fakeMedicineEvent)
        }
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
    override fun onNotificationClick(notificationId: Int, message: String) {
        val notificationManager =
            this@MainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(100)
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT).show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        intent?.categories?.let {
            if (intent.categories.contains(MEDICATION_EVENT_INTENT_CATEGORY))
                if (extras != null) {
                    if (extras.containsKey(GET_DRUG_ACTION_EXTRA_KEY)) {
                        onNotificationClick(extras.getInt(NOTIFICATION_ID_EXTRA_KEY),
                            extras.getString(GET_DRUG_ACTION_EXTRA_KEY).toString())
                    }
                    if (extras.containsKey(CANCEL_DRUG_ACTION_EXTRA_KEY)) {
                        onNotificationClick(extras.getInt(NOTIFICATION_ID_EXTRA_KEY),
                            extras.getString(CANCEL_DRUG_ACTION_EXTRA_KEY).toString())
                    }
                }
        }
    }
}