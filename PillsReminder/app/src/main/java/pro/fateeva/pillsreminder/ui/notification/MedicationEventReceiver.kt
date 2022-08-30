package pro.fateeva.pillsreminder.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import pro.fateeva.pillsreminder.R
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
        const val NOTIFICATION_TITLE_EXTRA_KEY = "NOTIFICATION_TITLE"
        const val NOTIFICATION_MESSAGE_EXTRA_KEY = "NOTIFICATION_MESSAGE"
        const val NOTIFICATION_REQUEST_CODE_EXTRA_KEY = "NOTIFICATION_REQUEST_CODE"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        onSuccessIntent: Intent?,
        onCancelIntent: Intent?,
        requestCode: Int,
    ) {
        val onSuccessPendingIntent =
            PendingIntent.getActivity(
                context,
                requestCode,
                onSuccessIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val onCancelPendingIntent =
            PendingIntent.getActivity(
                context,
                -requestCode,
                onCancelIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setOngoing(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .addAction(R.drawable.ic_accept_medication, "ПРИНЯТЬ", onSuccessPendingIntent)
                .addAction(R.drawable.ic_cancel_medication, "ОТМЕНИТЬ", onCancelPendingIntent)
                .setAutoCancel(true)

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
            (intent?.extras?.getString(NOTIFICATION_TITLE_EXTRA_KEY))
                ?: context.getString(R.string.notification_title_error)

        val drugName = (intent?.extras?.getString(NOTIFICATION_MESSAGE_EXTRA_KEY))
            ?: context.getString(R.string.drug_name_error)

        val requestCode =
            (intent?.extras?.getInt(NOTIFICATION_REQUEST_CODE_EXTRA_KEY)) ?: DEFAULT_REQUEST_CODE

        val onSuccessIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MainActivity.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MainActivity.GET_DRUG_ACTION_EXTRA_KEY, "Пользователь подтвердил прием лекарства")
            putExtra(MainActivity.NOTIFICATION_ID_EXTRA_KEY, requestCode)
        }

        val onCancelIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MainActivity.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MainActivity.CANCEL_DRUG_ACTION_EXTRA_KEY, "Пользователь отменил прием лекарства")
            putExtra(MainActivity.NOTIFICATION_ID_EXTRA_KEY, requestCode)
        }

        if (intent != null) {
            showNotification(
                context,
                notificationTitle,
                drugName,
                onSuccessIntent,
                onCancelIntent,
                requestCode)
        } else {
            Toast.makeText(context,
                context.getString(R.string.intent_error),
                Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(context,
            "${intent?.extras?.getString(NOTIFICATION_TITLE_EXTRA_KEY)}: " +
                    "${intent?.extras?.getString(NOTIFICATION_MESSAGE_EXTRA_KEY)}" +
                    " // REQ_CODE: ${intent?.extras?.getInt(NOTIFICATION_REQUEST_CODE_EXTRA_KEY)}",
            Toast.LENGTH_SHORT).show()
    }
}