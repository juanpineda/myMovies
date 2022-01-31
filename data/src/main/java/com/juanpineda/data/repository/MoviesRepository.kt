package com.juanpineda.data.repository

import com.juanpineda.data.result.ResultHandler
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.onSuccess
import com.juanpineda.data.source.LocalDataSource
import com.juanpineda.data.source.RemoteDataSource
import com.juanpineda.domain.Movie
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val regionRepository: RegionRepository,
    private val apiKey: String
) {
    lateinit var result: ResultHandler<Flow<List<Movie>>>
    suspend fun getPopularMovies(): ResultHandler<Flow<List<Movie>>> {
        if (localDataSource.isEmpty()) {
            remoteDataSource.getPopularMovies(apiKey, regionRepository.findLastRegion())
                .onSuccess {
                    localDataSource.saveMovies(it)
                    result = SuccessResponse(localDataSource.getPopularMovies())
                }
        } else {
            result = SuccessResponse(localDataSource.getPopularMovies())
        }
        return result
    }

    suspend fun findById(id: Int): Movie = localDataSource.findById(id)

    suspend fun update(movie: Movie) = localDataSource.update(movie)

    suspend fun getMovieImages(id: Int) = remoteDataSource.getMovieImages(apiKey, id)
}