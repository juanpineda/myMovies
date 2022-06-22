package com.juanpineda.mymovies

import android.app.Application
import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.data.repository.PermissionChecker
import com.juanpineda.data.repository.RegionRepository
import com.juanpineda.data.source.LocalDataSource
import com.juanpineda.data.source.LocationDataSource
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.mymovies.data.AndroidPermissionChecker
import com.juanpineda.mymovies.data.PlayServicesLocationDataSource
import com.juanpineda.mymovies.data.database.MovieDatabase
import com.juanpineda.mymovies.data.database.RoomDataSource
import com.juanpineda.mymovies.data.server.BASE_URL
import com.juanpineda.mymovies.data.server.TheMovieDb
import com.juanpineda.mymovies.data.server.TheMovieDbDataSource
import com.juanpineda.mymovies.ui.detail.DetailActivity
import com.juanpineda.mymovies.ui.detail.DetailViewModel
import com.juanpineda.mymovies.ui.main.MainActivity
import com.juanpineda.mymovies.ui.main.MainViewModel
import com.juanpineda.usecases.FindMovieById
import com.juanpineda.usecases.GetMovieImages
import com.juanpineda.usecases.GetPopularMovies
import com.juanpineda.usecases.RateMovie
import com.juanpineda.usecases.ToggleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single(named("apiKey")) { androidApplication().getString(R.string.api_key) }
    single { MovieDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource(get()) }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single(named("baseUrl")) { BASE_URL }
    single { TheMovieDb(get(named("baseUrl"))) }
}

val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("apiKey"))) }
}

private val scopesModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get(), get()) }
        scoped { GetPopularMovies(get()) }
    }

    scope(named<DetailActivity>()) {
        viewModel { (id: Int) -> DetailViewModel(id, get(), get(), get(), get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
        scoped { RateMovie(get()) }
        scoped { GetMovieImages(get()) }
    }
}