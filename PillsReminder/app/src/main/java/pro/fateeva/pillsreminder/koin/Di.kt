package pro.fateeva.pillsreminder.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepository
import pro.fateeva.pillsreminder.clean.data.MedicationReminderRepositoryImpl
import pro.fateeva.pillsreminder.clean.data.NotificationManager
import pro.fateeva.pillsreminder.clean.data.NotificationManagerImpl
import pro.fateeva.pillsreminder.clean.domain.MedicationInteractor
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsViewModel
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsViewModel
import pro.fateeva.pillsreminder.ui.screens.pillslist.PillsListViewModel

object Di {
    val mainModule = module {
        single<MedicationInteractor> { MedicationInteractor(get(), get()) }
        single<NotificationManager> { NotificationManagerImpl(get()) }
        single<MedicationReminderRepository> { MedicationReminderRepositoryImpl() }

        viewModel{ OncePerDaySettingsViewModel(get(), get()) }
        viewModel{ PillsListViewModel(get()) }
        viewModel{ TwicePerDaySettingsViewModel(get(), get()) }
    }
}