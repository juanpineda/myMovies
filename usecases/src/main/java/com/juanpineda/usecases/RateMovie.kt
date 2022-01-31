package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.domain.Movie

class RateMovie(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie) = moviesRepository.update(movie)
}