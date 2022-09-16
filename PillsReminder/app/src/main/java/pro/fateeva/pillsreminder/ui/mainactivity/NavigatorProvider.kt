package pro.fateeva.pillsreminder.ui.mainactivity

import pro.fateeva.pillsreminder.ui.navigation.AppNavigation

interface NavigatorProvider {
    fun provideAppNavigator(): AppNavigation
}