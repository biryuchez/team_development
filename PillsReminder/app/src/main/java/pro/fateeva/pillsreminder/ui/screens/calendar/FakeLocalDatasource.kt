package pro.fateeva.pillsreminder.ui.screens.calendar

import java.util.*
import kotlin.random.Random

private const val FAKE_MEDICATION_EVENTS_COUNT = 50

class FakeLocalDatasource : GetMedicationHistoryUsecase {
    override fun getMedicationHistory(): List<FakeMedicationHistoryEntity> {
        val medicationEventList = mutableListOf<FakeMedicationHistoryEntity>()

        for (i in -FAKE_MEDICATION_EVENTS_COUNT / 2..(-FAKE_MEDICATION_EVENTS_COUNT / 2) + FAKE_MEDICATION_EVENTS_COUNT) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 10)
            calendar.set(Calendar.MINUTE, 0)

            if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
                medicationEventList.add(FakeMedicationHistoryEntity(
                    medicationTime = calendar.timeInMillis,
                    medicationSuccessTime = if (Random.nextBoolean()) {
                        calendar.timeInMillis + (60000L..600000L).random()
                    } else {
                        -1L
                    }))
                if (Random.nextBoolean()) {
                    medicationEventList.add(FakeMedicationHistoryEntity(
                        medicationTime = calendar.timeInMillis,
                        pillName = "Но-шпа",
                        medicationSuccessTime = if (Random.nextBoolean()) {
                            calendar.timeInMillis + (60000L..600000L).random()
                        } else {
                            -1L
                        }))
                }

                calendar.set(Calendar.HOUR_OF_DAY, 20)

                medicationEventList.add(FakeMedicationHistoryEntity(
                    medicationTime = calendar.timeInMillis,
                    medicationSuccessTime = calendar.timeInMillis + (60000L..600000L).random()))
            }
        }
        return medicationEventList
    }
}