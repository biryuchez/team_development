package pro.fateeva.pillsreminder.ui.notification.medicationevents

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pro.fateeva.pillsreminder.R
import pro.fateeva.pillsreminder.domain.entity.medicationevent.MedicationEventDomain
import pro.fateeva.pillsreminder.ui.notification.MedicationEventReceiver

/**
 * Классы, отвечающие за формирование системного события о напоминании приема лекарства
 */
interface NotificationEvent {
    fun setEvent()
}


