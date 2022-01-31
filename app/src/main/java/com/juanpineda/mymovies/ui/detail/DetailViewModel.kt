package com.juanpineda.mymovies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    data class UiModel(
        val movie: Movie,
        val movieImages: List<MovieImage> = listOf()
    )

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) findMovie()
            return _model
        }

    private fun findMovie() = launch {
        _model.value = UiModel(findMovieById.invoke(movieId), getMovieImages.invoke(movieId))
    }

    fun onFavoriteClicked() = launch {
        _model.value?.movie?.let {
            _model.value = UiModel(toggleMovieFavorite.invoke(it))
        }
    }

    fun rateMovie(vote: Float) = launch {
        _model.value?.movie?.let {
            rateMovie.invoke(it.copy(myVote = vote))
        }
    }
}