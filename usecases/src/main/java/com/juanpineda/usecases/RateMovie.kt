package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.domain.Movie

class RateMovie(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movieId: Int, vote: Float): Movie =
        moviesRepository.rateMovie(movieId, vote)
}