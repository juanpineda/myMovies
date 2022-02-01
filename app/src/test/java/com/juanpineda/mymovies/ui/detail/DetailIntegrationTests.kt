package com.juanpineda.mymovies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.juanpineda.data.source.LocalDataSource
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.mymovies.FakeLocalDataSource
import com.juanpineda.mymovies.FakeRemoteDataSource
import com.juanpineda.mymovies.defaultFakeMovieImages
import com.juanpineda.mymovies.defaultFakeMovies
import com.juanpineda.mymovies.initMockedDi
import com.juanpineda.mymovies.testshared.mockedMovie
import com.juanpineda.usecases.FindMovieById
import com.juanpineda.usecases.GetMovieImages
import com.juanpineda.usecases.RateMovie
import com.juanpineda.usecases.ToggleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private lateinit var vm: DetailViewModel
    private lateinit var localDataSource: FakeLocalDataSource
    private lateinit var remoteDataSource: FakeRemoteDataSource

    @Before
    fun setUp() {
        val vmModule = module {
            factory { (id: Int) -> DetailViewModel(id, get(), get(), get(), get(), get()) }
            factory { FindMovieById(get()) }
            factory { ToggleMovieFavorite(get()) }
            factory { GetMovieImages(get()) }
            factory { RateMovie(get()) }
        }

        initMockedDi(vmModule)
        vm = get { parametersOf(1) }

        localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        remoteDataSource = get<RemoteDataSource>() as FakeRemoteDataSource
        localDataSource.movies = defaultFakeMovies
        remoteDataSource.movieImages = defaultFakeMovieImages
    }

    @Test
    fun `observing LiveData finds the movie`() {
        vm.model.observeForever(observer)

        verify(observer).onChanged(DetailViewModel.UiModel.ContentMovie(defaultFakeMovies[0]))
    }

    @Test
    fun `favorite is updated in local data source`() {
        vm.model.observeForever(observer)

        vm.onFavoriteClicked()

        runBlocking {
            assertTrue(localDataSource.findById(1).favorite)
        }
    }

    @Test
    fun `rate is updated in local data source`() {
        vm.model.observeForever(observer)

        vm.rateMovie(defaultFakeMovies[0].myVote)

        runBlocking {
            assertEquals(defaultFakeMovies[0].myVote, localDataSource.findById(1).myVote)
        }
    }
}