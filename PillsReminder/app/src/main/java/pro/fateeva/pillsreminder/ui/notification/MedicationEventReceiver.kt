package pro.fateeva.pillsreminder.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MedicationEventReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_KEY_NOTIFICATION_TITLE = "NOTIFICATION_TITLE"
        const val EXTRA_KEY_NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE"
        const val EXTRA_KEY_NOTIFICATION_REQUEST_CODE = "NOTIFICATION_REQUEST_CODE"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0,
            "${p1?.extras?.getString(EXTRA_KEY_NOTIFICATION_TITLE)}:" +
                    "${p1?.extras?.getString(EXTRA_KEY_NOTIFICATION_MESSAGE)}" +
                    " // REQ_CODE: ${p1?.extras?.getString(EXTRA_KEY_NOTIFICATION_REQUEST_CODE)}",
            Toast.LENGTH_SHORT).show()
    }
}