package com.juanpineda.mymovies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.juanpineda.data.result.ErrorResponse
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.error.Failure
import com.juanpineda.mymovies.testshared.mockedMovie
import com.juanpineda.mymovies.testshared.mockedMovieImage
import com.juanpineda.usecases.FindMovieById
import com.juanpineda.usecases.GetMovieImages
import com.juanpineda.usecases.RateMovie
import com.juanpineda.usecases.ToggleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var findMovieById: FindMovieById

    @Mock
    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Mock
    lateinit var getMovieImages: GetMovieImages

    @Mock
    lateinit var rateMovie: RateMovie

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private lateinit var vm: DetailViewModel

    @Before
    fun setUp() {
        vm = DetailViewModel(
            1,
            findMovieById,
            toggleMovieFavorite,
            getMovieImages,
            rateMovie,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `observing LiveData finds the movie`() {

        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)

            vm.model.observeForever(observer)

            verify(observer).onChanged(DetailViewModel.UiModel.ContentMovie(movie))

        }
    }

    @Test
    fun `observing LiveData find the movieImages`() {

        runBlocking {
            val movieImages = listOf(mockedMovieImage)
            whenever(getMovieImages.invoke(1)).thenReturn(SuccessResponse(movieImages))

            vm.model.observeForever(observer)

            verify(observer).onChanged(DetailViewModel.UiModel.ContentMovieImages(movieImages))

        }
    }

    @Test
    fun `observing LiveData error at find movieImages`() {

        runBlocking {
            val movieImages = listOf(mockedMovieImage)
            whenever(getMovieImages.invoke(1)).thenReturn(ErrorResponse(Failure.UnknownException))

            vm.model.observeForever(observer)

            verify(observer).onChanged(DetailViewModel.UiModel.ContentMovieImages(listOf()))

        }
    }

    @Test
    fun `when favorite clicked, the toggleMovieFavorite use case is invoked`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(toggleMovieFavorite.invoke(1)).thenReturn(movie.copy(favorite = !movie.favorite))
            vm.model.observeForever(observer)

            vm.onFavoriteClicked()

            verify(toggleMovieFavorite).invoke(1)
        }
    }

    @Test
    fun `when rate clicked, the rateMovie use case is invoked`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1, myVote = 1f)
            whenever(rateMovie.invoke(movie.id, movie.myVote)).thenReturn(movie)
            vm.model.observeForever(observer)

            vm.rateMovie(movie.myVote)

            verify(rateMovie).invoke(movie.id, movie.myVote)
        }
    }
}