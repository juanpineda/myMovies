package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository
import com.juanpineda.data.result.ResultHandler
import com.juanpineda.domain.Movie
import kotlinx.coroutines.flow.Flow

class GetPopularMovies(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(): ResultHandler<Flow<List<Movie>>> = moviesRepository.getPopularMovies()
}