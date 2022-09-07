package pro.fateeva.pillsreminder.clean

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.fateeva.pillsreminder.ui.screens.twiceperday.TwicePerDaySettingsViewModel
import pro.fateeva.pillsreminder.ui.screens.onceperday.OncePerDaySettingsViewModel

object Di {
    val mainModule = module {
        single<MedicationInteractor> { MedicationInteractor(get(), get()) }
        single<NotificationManager> { NotificationManagerImpl(get()) }
        single<MedicationReminderRepository> { MedicationReminderRepositoryImpl() }

        viewModel{ OncePerDaySettingsViewModel(get(), get()) }
        viewModel{ TwicePerDaySettingsViewModel(get()) }
    }
}