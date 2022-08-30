package pro.fateeva.pillsreminder.ui.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain

/**
 * Классы, отвечающие за формирование системного события о напоминании приема лекарства
 */
interface NotificationEvent {
    fun setEvent()

    abstract class BaseNotificationEvent(
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

    class Single(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ) : BaseNotificationEvent(medicationEvent, eventReminder, context) {
        override fun setEvent() {
            eventReminder.setExact(
                AlarmManager.RTC_WAKEUP,
                medicationEvent.firstMedicationTime(),
                pendingEventIntent)
        }
    }

    class Repeating(
        medicationEvent: MedicationEventDomain,
        eventReminder: AlarmManager,
        context: Context,
    ) : BaseNotificationEvent(medicationEvent, eventReminder, context) {
        override fun setEvent() {
            TODO("Not yet implemented")
            // тут будет реализовано повторяющееся напоминание
            // сейчас не реализовано, т.к. повторяющиеся события надо программно отменять,
            // иначе они будут повторяться, пока устройство не будет перезагружено.
            // реализуем, когда появится интерфейс
        }
    }
}


