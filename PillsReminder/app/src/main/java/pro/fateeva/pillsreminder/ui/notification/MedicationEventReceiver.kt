package pro.fateeva.pillsreminder.ui.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.MainActivity

private const val DEFAULT_REQUEST_CODE = -1
private const val CHANNEL_ID = "Medication_channel"

/**
 * Ресивер интентов от AlarmManager.
 * Когда AlarmManager сообщает, что настало время события "Прием лекарства", ресивер
 * посылает уведомление.
 * Если в момент клика на уведомление приложение на экране - выполнится заданное в MainActivity
 * действие. Если приложение свернуто либо выгружено из памяти - по клику на уведомление запустится
 * приложение и выполнится заданное в MainActivity действие.
 */
class MedicationEventReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_KEY_NOTIFICATION_TITLE = "NOTIFICATION_TITLE"
        const val EXTRA_KEY_NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE"
        const val EXTRA_KEY_NOTIFICATION_REQUEST_CODE = "NOTIFICATION_REQUEST_CODE"
        const val EXTRA_KEY_MEDICATION_EVENT = "MEDICATION_EVENT"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        intent: Intent?,
        requestCode: Int,
    ) {
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH))
        }

        notificationManager.notify(requestCode, notificationBuilder.build())
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val notificationTitle =
            (intent?.extras?.getString(EXTRA_KEY_NOTIFICATION_TITLE))
                ?: context.getString(R.string.notification_title_error)

        val drugName = (intent?.extras?.getString(EXTRA_KEY_NOTIFICATION_MESSAGE))
            ?: context.getString(R.string.drug_name_error)

        val requestCode =
            (intent?.extras?.getInt(EXTRA_KEY_NOTIFICATION_REQUEST_CODE)) ?: DEFAULT_REQUEST_CODE

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MainActivity.EVENT_INTENT_CATEGORY)
        }

        if (intent != null) {
            showNotification(
                context,
                notificationTitle,
                drugName,
                activityIntent,
                requestCode)
        } else {
            Toast.makeText(context,
                context.getString(R.string.intent_error),
                Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(context,
            "${intent?.extras?.getString(EXTRA_KEY_NOTIFICATION_TITLE)}: " +
                    "${intent?.extras?.getString(EXTRA_KEY_NOTIFICATION_MESSAGE)}" +
                    " // REQ_CODE: ${intent?.extras?.getInt(EXTRA_KEY_NOTIFICATION_REQUEST_CODE)}",
            Toast.LENGTH_SHORT).show()

        val event = (intent?.extras?.getSerializable(EXTRA_KEY_MEDICATION_EVENT) as MedicationEventDomain)
        if (event is MedicationEventDomain.Repeating){
            event.setFirstMedicationTime(event.firstMedicationTime() + event.medicationInterval())

                val notificationEvent: NotificationEvent = EventFactory.NotificationEventFactory()
                    .generateNotificationEvent(
                        medicationEvent = event,
                        eventReminder = getSystemService(context, AlarmManager::class.java) as AlarmManager,
                        context = context
                    )

                notificationEvent.setEvent()
        }
    }
}