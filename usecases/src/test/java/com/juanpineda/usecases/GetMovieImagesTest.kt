package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.data.result.ErrorResponse
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.error.Failure
import com.juanpineda.data.result.onError
import com.juanpineda.data.result.onSuccess
import com.juanpineda.mymovies.testshared.mockedMovieImage
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieImagesTest {

    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var getMovieImages: GetMovieImages

    @Before
    fun setUp() {
        getMovieImages = GetMovieImages(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository return success`() {
        runBlocking {

            val movieImages = listOf(mockedMovieImage)
            whenever(moviesRepository.getMovieImages(1)).thenReturn(SuccessResponse(movieImages))

            getMovieImages.invoke(1).onSuccess {
                assertEquals(movieImages, it)
            }
        }
    }

    @Test
    fun `invoke calls movies repository return error UnknownException`() {
        runBlocking {

            whenever(moviesRepository.getMovieImages(1)).thenReturn(ErrorResponse(Failure.UnknownException))

            getMovieImages.invoke(1).onError {
                assertEquals(Failure.UnknownException, failure)
            }
        }
    }

    @Test
    fun `invoke calls movies repository return error NetworkConnection`() {
        runBlocking {
            val networkConnection = Failure.NetworkConnection(Exception())
            whenever(moviesRepository.getMovieImages(1)).thenReturn(ErrorResponse(networkConnection))

            getMovieImages.invoke(1).onError {
                assertEquals(networkConnection, failure)
            }
        }
    }
}