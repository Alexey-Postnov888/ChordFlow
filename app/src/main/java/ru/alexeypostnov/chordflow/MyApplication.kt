package ru.alexeypostnov.chordflow

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.alexeypostnov.chordflow.di.databaseModule
import ru.alexeypostnov.chordflow.di.networkModule
import ru.alexeypostnov.chordflow.di.repositoryModule
import ru.alexeypostnov.chordflow.di.useCaseModule
import ru.alexeypostnov.chordflow.di.utilsModule
import ru.alexeypostnov.chordflow.di.viewModelModule

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                useCaseModule,
                utilsModule,
                viewModelModule
            )
        }
    }
}