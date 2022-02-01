package com.juanpineda.data.repository

import com.juanpineda.data.result.ResultHandler
import com.juanpineda.data.result.SuccessResponse
import com.juanpineda.data.result.onError
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

    suspend fun getPopularMovies(): ResultHandler<Flow<List<Movie>>> {
        lateinit var result: ResultHandler<Flow<List<Movie>>>
        if (localDataSource.isEmpty())
            remoteDataSource.getPopularMovies(apiKey, regionRepository.findLastRegion())
                .onSuccess {
                    localDataSource.saveMovies(it)
                    result = SuccessResponse(localDataSource.getPopularMovies())
                }.onError {
                    result = this
                }
        else result = SuccessResponse(localDataSource.getPopularMovies())
        return result
    }

    suspend fun findById(id: Int): Movie = localDataSource.findById(id)

    suspend fun toggleMovieFavorite(movieId: Int): Movie =
        with(localDataSource.findById(movieId)) {
            copy(favorite = !favorite).also { localDataSource.update(it) }
        }

    suspend fun rateMovie(movieId: Int, vote: Float): Movie =
        with(localDataSource.findById(movieId)) {
            copy(myVote = vote).also { localDataSource.update(it) }
        }

    suspend fun getMovieImages(id: Int) = remoteDataSource.getMovieImages(apiKey, id)
}