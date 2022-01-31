package com.juanpineda.data.result.error

import java.net.ConnectException

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure(val error: Exception? = null) {
    class NetworkConnection(error: Exception? = null) : Failure(error)
    object UnknownException : Failure()

    companion object {
        fun analyzeException(exception: Exception?) =
            when (exception) {
                is ConnectException -> NetworkConnection(exception)
                else -> UnknownException
            }
    }
}