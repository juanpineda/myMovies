package com.juanpineda.mymovies.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.juanpineda.data.result.ErrorResponse
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.error.Failure
import com.juanpineda.mymovies.testshared.mockedMovie
import com.juanpineda.mymovies.ui.main.MainViewModel.UiModel
import com.juanpineda.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getPopularMovies: GetPopularMovies

    @Mock
    lateinit var observer: Observer<UiModel>

    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        vm = MainViewModel(getPopularMovies, Dispatchers.Unconfined)
    }

    @Test
    fun `observing LiveData launches location permission request`() {

        vm.model.observeForever(observer)

        verify(observer).onChanged(UiModel.RequestLocationPermission)
    }

    @Test
    fun `after requesting the permission, loading is shown`() {
        runBlocking {

            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getPopularMovies.invoke()).thenReturn(SuccessResponse(flowOf(movies)))
            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(UiModel.Loading)
        }
    }

    @Test
    fun `after requesting the permission, getPopularMovies is called`() {

        runBlocking {
            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getPopularMovies.invoke()).thenReturn(SuccessResponse(flowOf(movies)))

            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(UiModel.Content(movies))
        }
    }

    @Test
    fun `after requesting the permission, error is invoked`() {

        runBlocking {
            whenever(getPopularMovies.invoke()).thenReturn(ErrorResponse(Failure.UnknownException))

            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(UiModel.Error)
        }
    }

    @Test
    fun `on Movie clicked, go to navigation`() {

        runBlocking {
            val movie = mockedMovie

            vm.model.observeForever(observer)

            vm.onMovieClicked(movie)

            verify(observer).onChanged(UiModel.Navigation(movie))
        }
    }
}