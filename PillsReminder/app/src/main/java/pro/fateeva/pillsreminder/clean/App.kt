package pro.fateeva.pillsreminder.clean

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(Di.mainModule)
            androidContext(this@App)
        }
    }
}

val Context.app: App
    get() {
        return applicationContext as App
    }