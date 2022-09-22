package pro.fateeva.pillsreminder.clean.data.room.fake

import pro.fateeva.pillsreminder.clean.data.room.MedicationEntityMapper
import pro.fateeva.pillsreminder.clean.domain.entity.MedicationScheduleItemDomain
import java.util.*
import kotlin.random.Random

private const val FAKE_MEDICATION_EVENTS_COUNT = 50

class FakeFakeLocalRepositoryImpl : FakeLocalRepository {

    // имитация Dao, пока нет реализации room
    private val fakeDao = object : FakeDao {
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
                            pillId = 7777,
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

    override fun getMedicationSchedule(): List<MedicationScheduleItemDomain> {
        return fakeDao.getMedicationSchedule()
            .map { MedicationEntityMapper().convertToMedicationScheduleItemDomain(it) }
    }
}

// имитация Dao, пока нет реализации room
interface FakeDao {
    fun getMedicationSchedule(): List<FakeMedicationScheduleEntity>
}