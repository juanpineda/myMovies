package com.juanpineda.mymovies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.juanpineda.data.result.onError
import com.juanpineda.data.result.onSuccess
import com.juanpineda.domain.Movie
import com.juanpineda.domain.MovieImage
import com.juanpineda.mymovies.ui.common.ScopedViewModel
import com.juanpineda.usecases.FindMovieById
import com.juanpineda.usecases.GetMovieImages
import com.juanpineda.usecases.RateMovie
import com.juanpineda.usecases.ToggleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieId: Int,
    private val findMovieById: FindMovieById,
    private val toggleMovieFavorite: ToggleMovieFavorite,
    private val getMovieImages: GetMovieImages,
    private val rateMovie: RateMovie,
    override val uiDispatcher: CoroutineDispatcher
) :
    ScopedViewModel(uiDispatcher) {

    sealed class UiModel {
        data class ContentMovie(val movie: Movie) : UiModel()
        data class ContentMovieImages(val movieImages: List<MovieImage>) : UiModel()
        data class ContentMovieFavorite(val movie: Movie) : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) findMovie()
            return _model
        }

    private fun findMovie() = launch {
        getMovieImages.invoke(movieId)
            .onSuccess {
                _model.value = UiModel.ContentMovieImages(it)
            }.onError {
                _model.value = UiModel.ContentMovieImages(listOf())
            }
        _model.value = UiModel.ContentMovie(findMovieById.invoke(movieId))
    }

    fun onFavoriteClicked() = launch {
        _model.value = UiModel.ContentMovieFavorite(toggleMovieFavorite.invoke(movieId))
    }

    fun rateMovie(vote: Float) = launch {
        _model.value = UiModel.ContentMovie(rateMovie.invoke(movieId, vote))
    }
}