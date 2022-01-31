package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.onSuccess
import com.juanpineda.mymovies.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetPopularMoviesTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var getPopularMovies: GetPopularMovies

    @Before
    fun setUp() {
        getPopularMovies = GetPopularMovies(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {

            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(moviesRepository.getPopularMovies()).thenReturn(SuccessResponse(movies))

            getPopularMovies.invoke().onSuccess {
                Assert.assertEquals(movies, it)
            }
        }
    }
}