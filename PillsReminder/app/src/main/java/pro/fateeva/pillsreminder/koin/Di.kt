package pro.fateeva.pillsreminder.koin

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepositoryImpl
import pro.fateeva.pillsreminder.clean.data.NotificationManager
import pro.fateeva.pillsreminder.clean.data.NotificationManagerImpl
import pro.fateeva.pillsreminder.clean.data.room.*
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.ui.screens.calendar.ScheduleCalendarViewModel
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsViewModel
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListViewModel
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsViewModel

object Di {
    val mainModule = module {
        single<MedicationInteractor> { MedicationInteractor(get(), get()) }
        single<NotificationManager> { NotificationManagerImpl(get()) }
        single<MedicationReminderRepository> { MedicationReminderRepositoryImpl(medicationDao = get(), mapper = get()) }
        single<FakeLocalRepository> { FakeFakeLocalRepositoryImpl() }
        factory { MedicationEntityMapper() }

        viewModel { OncePerDaySettingsViewModel(get(), get()) }
        viewModel { PillsListViewModel(get()) }
        viewModel { TwicePerDaySettingsViewModel(get(), get()) }
        viewModel { ScheduleCalendarViewModel(fakeLocalRepository = get()) }
    }

    val roomModule = module {
        single { LocalMedicationDatabase.getUserDatabase(androidContext()).medicationDao }
    }
}