package com.juanpineda.data.source

import com.juanpineda.domain.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun getPopularMovies(): Flow<List<Movie>>
    suspend fun findById(id: Int): Movie
    suspend fun update(movie: Movie)
}