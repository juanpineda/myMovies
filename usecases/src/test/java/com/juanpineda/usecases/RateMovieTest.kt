package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.mymovies.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RateMovieTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var rateMovie: RateMovie

    @Before
    fun setUp() {
        rateMovie = RateMovie(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {
            val vote = 3f
            val movie = mockedMovie.copy(id = 1)
            val movieUpdate = movie.copy(myVote = vote)
            whenever(
                moviesRepository.rateMovie(
                    movie.id,
                    vote
                )
            ).thenReturn(movieUpdate)

            val result = rateMovie.invoke(movie.id, vote)

            assertEquals(movieUpdate, result)
        }
    }
}