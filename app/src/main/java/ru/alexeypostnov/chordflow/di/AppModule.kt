package ru.alexeypostnov.chordflow.di

import androidx.room.Room
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.alexeypostnov.chordflow.data.db.AuthorsDAO
import ru.alexeypostnov.chordflow.data.db.ChordFlowDatabase
import ru.alexeypostnov.chordflow.data.db.SongsDAO
import ru.alexeypostnov.chordflow.data.network.ChordService
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepository
import ru.alexeypostnov.chordflow.data.repository.ChordDatabaseRepositoryImpl
import ru.alexeypostnov.chordflow.data.repository.ChordRepository
import ru.alexeypostnov.chordflow.data.repository.ChordRepositoryImpl
import ru.alexeypostnov.chordflow.data.utils.ChordParser
import ru.alexeypostnov.chordflow.data.utils.ChordParserImpl
import ru.alexeypostnov.chordflow.domain.DeleteSongByIdUseCase
import ru.alexeypostnov.chordflow.domain.DeleteSongByIdUseCaseImpl
import ru.alexeypostnov.chordflow.domain.GetAuthorsListUseCase
import ru.alexeypostnov.chordflow.domain.GetAuthorsListUseCaseImpl
import ru.alexeypostnov.chordflow.domain.GetSongDetailsByIdUseCase
import ru.alexeypostnov.chordflow.domain.GetSongDetailsByIdUseCaseImpl
import ru.alexeypostnov.chordflow.domain.GetSongsListByAuthorUseCase
import ru.alexeypostnov.chordflow.domain.GetSongsListByAuthorUseCaseImpl
import ru.alexeypostnov.chordflow.domain.PostCreateSongUseCase
import ru.alexeypostnov.chordflow.domain.PostCreateSongUseCaseImpl
import ru.alexeypostnov.chordflow.domain.PutEditSongByIdUseCase
import ru.alexeypostnov.chordflow.domain.PutEditSongByIdUseCaseImpl
import ru.alexeypostnov.chordflow.presentation.authorsList.AuthorsListViewModel
import ru.alexeypostnov.chordflow.presentation.createSong.CreateSongViewModel
import ru.alexeypostnov.chordflow.presentation.editSong.EditSongViewModel
import ru.alexeypostnov.chordflow.presentation.songDetails.SongDetailsViewModel
import ru.alexeypostnov.chordflow.presentation.songsList.SongsListViewModel
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://chordflow.alexeypostnov.ru/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<ChordService>()
    } bind ChordService::class

    single {
        OkHttpClient.Builder()
//            .addInterceptor(get<AuthIntercept>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    } bind OkHttpClient::class

    single {
        Room.databaseBuilder(
            get(),
            ChordFlowDatabase::class.java,
            "chordflow.db"
        )
            .addMigrations(ChordFlowDatabase.MIGRATION_0_1)
            .build()
    }

    single { get<ChordFlowDatabase>().authorsDAO } bind AuthorsDAO::class
    single { get<ChordFlowDatabase>().songsDAO } bind SongsDAO::class

    singleOf(::ChordRepositoryImpl) bind ChordRepository::class
    singleOf(::ChordDatabaseRepositoryImpl) bind ChordDatabaseRepository::class

    singleOf(::GetSongDetailsByIdUseCaseImpl) bind GetSongDetailsByIdUseCase::class
    singleOf(::PostCreateSongUseCaseImpl) bind PostCreateSongUseCase::class
    singleOf(::DeleteSongByIdUseCaseImpl) bind DeleteSongByIdUseCase::class
    singleOf(::PutEditSongByIdUseCaseImpl) bind PutEditSongByIdUseCase::class
    singleOf(::GetAuthorsListUseCaseImpl) bind GetAuthorsListUseCase::class
    singleOf(::GetSongsListByAuthorUseCaseImpl) bind GetSongsListByAuthorUseCase::class

    singleOf(::ChordParserImpl) bind ChordParser::class
}

val viewModelModule = module {
    viewModel { SongsListViewModel(get()) }
    viewModel { SongDetailsViewModel(get(), get(), get()) }
    viewModel { CreateSongViewModel(get()) }
    viewModel { EditSongViewModel(get(), get()) }
    viewModel { AuthorsListViewModel(get()) }
}