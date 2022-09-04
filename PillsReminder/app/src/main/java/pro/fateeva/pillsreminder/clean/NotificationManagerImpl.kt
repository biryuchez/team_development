package pro.fateeva.pillsreminder.clean

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.ui.notification.MedicationEventReceiver
import pro.fateeva.pillsreminder.ui.notification.notificationcreator.MedicationNotifier

class NotificationManagerImpl(
    private val context: Context
) : NotificationManager {

    val alarmManager: AlarmManager by lazy {
        context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    }

    override fun planNotification(medicationReminder: MedicationReminder, notificationTime : Long) {
        val medicationReminderIntent = Intent(context, MedicationEventReceiver::class.java).apply {

            putExtra(
                MedicationNotifier.NOTIFICATION_TITLE_EXTRA_KEY,
                context.getString(R.string.its_time_to_medication)
            )

            putExtra(
                MedicationNotifier.NOTIFICATION_DRUG_NAME_EXTRA_KEY,
                medicationReminder.medicationName
            )

            putExtra(MedicationNotifier.NOTIFICATION_DOSAGE_EXTRA_KEY, medicationReminder.dosage)

            putExtra(
                MedicationNotifier.NOTIFICATION_ID_EXTRA_KEY,
                medicationReminder.id
            )

            putExtra(
                MedicationNotifier.REMINDER_TIME_EXTRA_KEY,
                notificationTime
            )
        }

        val pendingEventIntent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                medicationReminder.id,
                medicationReminderIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingEventIntent
            )
    }
}