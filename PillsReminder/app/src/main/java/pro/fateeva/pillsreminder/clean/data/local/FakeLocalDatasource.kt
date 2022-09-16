package pro.fateeva.pillsreminder.clean.data.local

import pro.fateeva.pillsreminder.clean.data.local.entity.FakeMedicationScheduleEntity
import pro.fateeva.pillsreminder.clean.domain.usecase.GetMedicationScheduleUsecase
import java.util.*
import kotlin.random.Random

private const val FAKE_MEDICATION_EVENTS_COUNT = 50

class FakeLocalDatasource : GetMedicationScheduleUsecase {
    override fun getMedicationSchedule(): List<FakeMedicationScheduleEntity> {
        val medicationEventList = mutableListOf<FakeMedicationScheduleEntity>()

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
                medicationEventList.add(FakeMedicationScheduleEntity(
                    medicationTime = calendar.timeInMillis,
                    medicationSuccessTime = if (Random.nextBoolean()) {
                        calendar.timeInMillis + (60000L..600000L).random()
                    } else {
                        -1L
                    }))
                if (Random.nextBoolean()) {
                    medicationEventList.add(FakeMedicationScheduleEntity(
                        medicationTime = calendar.timeInMillis,
                        pillName = "Но-шпа",
                        medicationSuccessTime = if (Random.nextBoolean()) {
                            calendar.timeInMillis + (60000L..600000L).random()
                        } else {
                            -1L
                        }))
                }

                calendar.set(Calendar.HOUR_OF_DAY, 20)

                medicationEventList.add(FakeMedicationScheduleEntity(
                    medicationTime = calendar.timeInMillis,
                    medicationSuccessTime = calendar.timeInMillis + (60000L..600000L).random()))
            }
        }
        return medicationEventList
    }
}