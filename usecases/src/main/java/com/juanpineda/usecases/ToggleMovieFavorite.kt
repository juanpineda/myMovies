package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int): Movie = moviesRepository.toggleMovieFavorite(id)
}