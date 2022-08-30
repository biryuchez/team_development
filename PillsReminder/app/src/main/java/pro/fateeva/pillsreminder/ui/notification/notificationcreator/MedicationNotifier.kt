package pro.fateeva.pillsreminder.ui.notification.notificationcreator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.ui.MainActivity
import pro.fateeva.pillsreminder.ui.notification.actionlistener.MedicationActionListener

private const val DEFAULT_REQUEST_CODE = -1
private const val CHANNEL_ID = "Medication_channel"

class MedicationNotifier : MedicationNotification {

    companion object {
        const val NOTIFICATION_TITLE_EXTRA_KEY = "NOTIFICATION_TITLE"
        const val NOTIFICATION_MESSAGE_EXTRA_KEY = "NOTIFICATION_MESSAGE"
        const val NOTIFICATION_REQUEST_CODE_EXTRA_KEY = "NOTIFICATION_REQUEST_CODE"
    }

    override fun showNotification(context: Context, intent: Intent) {
        val notificationTitle =
            (intent.extras?.getString(NOTIFICATION_TITLE_EXTRA_KEY))
                ?: context.getString(R.string.notification_title_error)

        val drugName = (intent.extras?.getString(NOTIFICATION_MESSAGE_EXTRA_KEY))
            ?: context.getString(R.string.drug_name_error)

        val requestCode =
            (intent.extras?.getInt(NOTIFICATION_REQUEST_CODE_EXTRA_KEY)) ?: DEFAULT_REQUEST_CODE

        val onGetDrugIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MedicationActionListener.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MedicationActionListener.GET_DRUG_ACTION_EXTRA_KEY,
                "(ТЕСТ) Пользователь подтвердил прием лекарства")
            putExtra(MedicationActionListener.NOTIFICATION_ID_EXTRA_KEY, requestCode)
        }

        val onCancelDrugIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addCategory(MedicationActionListener.MEDICATION_EVENT_INTENT_CATEGORY)
            putExtra(MedicationActionListener.CANCEL_DRUG_ACTION_EXTRA_KEY,
                "(ТЕСТ) Пользователь отменил прием лекарства")
            putExtra(MedicationActionListener.NOTIFICATION_ID_EXTRA_KEY,
                requestCode)
        }

        val onSuccessPendingIntent =
            PendingIntent.getActivity(
                context,
                requestCode,
                onGetDrugIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val onCancelPendingIntent =
            PendingIntent.getActivity(
                context,
                -requestCode,
                onCancelDrugIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(notificationTitle)
                .setContentText(drugName)
                .setOngoing(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
                .addAction(R.drawable.ic_accept_medication,
                    context.getString(R.string.get_drug_notification_button),
                    onSuccessPendingIntent)
                .addAction(R.drawable.ic_cancel_medication,
                    context.getString(R.string.cancel_drug_notification_button),
                    onCancelPendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH))
        }

        notificationManager.notify(requestCode, notificationBuilder.build())
    }
}

