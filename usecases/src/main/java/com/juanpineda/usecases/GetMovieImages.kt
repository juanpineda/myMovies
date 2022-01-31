package com.juanpineda.usecases

import com.juanpineda.data.repository.MoviesRepository

class GetMovieImages(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int) = moviesRepository.getMovieImages(id)
}