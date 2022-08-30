package pro.fateeva.pillsreminder.ui.notification.medicationevents

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.MedicationEventReceiver
import pro.fateeva.pillsreminder.ui.notification.notificationcreator.MedicationNotifier

abstract class BasicNotificationEvent(
    protected val medicationEvent: MedicationEventDomain,
    protected val eventReminder: AlarmManager,
    context: Context,
) : NotificationEvent {
    private val eventIntent = Intent(context, MedicationEventReceiver::class.java).apply {
        putExtra(MedicationNotifier.NOTIFICATION_TITLE_EXTRA_KEY,
            context.getString(R.string.its_time_to_medication))
        putExtra(MedicationNotifier.NOTIFICATION_MESSAGE_EXTRA_KEY,
            medicationEvent.drugName())
        putExtra(MedicationNotifier.NOTIFICATION_REQUEST_CODE_EXTRA_KEY,
            medicationEvent.id())
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    protected val pendingEventIntent: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            medicationEvent.id(),
            eventIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
}