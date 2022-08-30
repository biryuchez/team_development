package pro.fateeva.pillsreminder.ui.notification

import android.content.Context
import android.content.Intent

interface MedicationNotification {
    fun showNotification(context: Context, intent: Intent)
}