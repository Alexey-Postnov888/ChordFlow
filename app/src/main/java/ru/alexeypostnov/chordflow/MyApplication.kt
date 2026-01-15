package ru.alexeypostnov.chordflow

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.alexeypostnov.chordflow.di.appModule
import ru.alexeypostnov.chordflow.di.viewModelModule

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule,
                viewModelModule
            )
        }
    }
}