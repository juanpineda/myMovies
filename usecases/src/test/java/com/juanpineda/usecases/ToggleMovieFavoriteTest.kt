package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.mymovies.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToggleMovieFavoriteTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Before
    fun setUp() {
        toggleMovieFavorite = ToggleMovieFavorite(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 1)

            toggleMovieFavorite.invoke(movie.id)

            verify(moviesRepository).toggleMovieFavorite(movie.id)
        }
    }

    @Test
    fun `favorite movie becomes unfavorite`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 1, favorite = true)
            whenever(moviesRepository.toggleMovieFavorite(1)).thenReturn(
                movie.copy(favorite = false)
            )
            val result = toggleMovieFavorite.invoke(1)

            assertFalse(result.favorite)
        }
    }
}