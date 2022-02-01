package com.juanpineda.mymovies

import com.juanpineda.data.repository.PermissionChecker
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.source.LocalDataSource
import com.juanpineda.data.source.LocationDataSource
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.domain.Movie
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.testshared.mockedMovie
import com.juanpineda.mymovies.testshared.mockedMovieImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module {
    single(named("apiKey")) { "12456" }
    single<LocalDataSource> { FakeLocalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<PermissionChecker> { FakePermissionChecker() }
    single { Dispatchers.Unconfined }
}

val defaultFakeMovies = listOf(
    mockedMovie.copy(1, myVote = 2f),
    mockedMovie.copy(2),
    mockedMovie.copy(3),
    mockedMovie.copy(4)
)

val defaultFakeMovieImages = listOf(
    mockedMovieImage.copy(1.0),
    mockedMovieImage.copy(2.0),
    mockedMovieImage.copy(3.0),
    mockedMovieImage.copy(4.0)
)

class FakeLocalDataSource : LocalDataSource {

    var movies: List<Movie> = emptyList()

    override suspend fun isEmpty() = movies.isEmpty()

    override suspend fun saveMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override suspend fun getPopularMovies(): Flow<List<Movie>> = flowOf(movies)

    override suspend fun findById(id: Int): Movie = movies.first { it.id == id }

    override suspend fun update(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }
}

class FakeRemoteDataSource : RemoteDataSource {

    private var movies = defaultFakeMovies
    var movieImages: List<MovieImage> = emptyList()

    override suspend fun getPopularMovies(apiKey: String, region: String) = SuccessResponse(movies)

    override suspend fun getMovieImages(apiKey: String, movieId: Int) = SuccessResponse(movieImages)
}

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String? = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override suspend fun check(permission: PermissionChecker.Permission): Boolean =
        permissionGranted
}