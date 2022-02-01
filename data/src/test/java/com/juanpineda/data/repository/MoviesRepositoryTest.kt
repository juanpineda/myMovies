package com.juanpineda.data.repository

import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.onSuccess
import com.juanpineda.data.source.LocalDataSource
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.mymovies.testshared.mockedMovie
import com.juanpineda.mymovies.testshared.mockedMovieImage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {

    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var regionRepository: RegionRepository

    lateinit var moviesRepository: MoviesRepository

    private val apiKey = "1a2b3c4d"

    @Before
    fun setUp() {
        moviesRepository =
            MoviesRepository(localDataSource, remoteDataSource, regionRepository, apiKey)
    }

    @Test
    fun `getPopularMovies gets from local data source first`() {
        runBlocking {

            val localMovies = listOf(mockedMovie.copy(1))
            whenever(localDataSource.isEmpty()).thenReturn(false)
            whenever(localDataSource.getPopularMovies()).thenReturn(flowOf(localMovies))

            moviesRepository.getPopularMovies().onSuccess {
                it.collect {
                    assertEquals(localMovies, it)
                }
            }
        }
    }

    @Test
    fun `getPopularMovies saves remote data to local`() {
        runBlocking {

            val remoteMovies = listOf(mockedMovie.copy(2))
            whenever(localDataSource.isEmpty()).thenReturn(true)
            whenever(remoteDataSource.getPopularMovies(any(), any())).thenReturn(
                SuccessResponse(
                    remoteMovies
                )
            )
            whenever(regionRepository.findLastRegion()).thenReturn("US")

            moviesRepository.getPopularMovies()

            verify(localDataSource).saveMovies(remoteMovies)
        }
    }

    @Test
    fun `findById calls local data source`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 5)
            whenever(localDataSource.findById(5)).thenReturn(movie)

            val result = moviesRepository.findById(5)

            assertEquals(movie, result)
        }
    }

    @Test
    fun `toggleMovieFavorite updates local data source`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 1)
            whenever(localDataSource.findById(1)).thenReturn(movie)

            moviesRepository.toggleMovieFavorite(1)

            verify(localDataSource).update(movie.copy(favorite = !movie.favorite))
        }
    }

    @Test
    fun `rateMovie updates local data source`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 1, myVote = 4f)
            whenever(localDataSource.findById(1)).thenReturn(movie)

            moviesRepository.rateMovie(1, 4f)

            verify(localDataSource).update(movie)
        }
    }

    @Test
    fun `getMovieImages calls local data source`() {
        runBlocking {

            val movieImages = listOf(mockedMovieImage)
            whenever(remoteDataSource.getMovieImages(apiKey, 5)).thenReturn(
                SuccessResponse(
                    movieImages
                )
            )

            moviesRepository.getMovieImages(5).onSuccess {
                assertEquals(movieImages, it)
            }
        }
    }
}