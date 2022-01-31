package com.juanpineda.data.result

import com.juanpineda.data.result.error.Failure


sealed class ResultHandler<out T : Any?>
class SuccessResponse<out T : Any?>(val data: T) : ResultHandler<T>()
class ErrorResponse(val failure: Failure) : ResultHandler<Nothing>()

inline fun <T : Any> ResultHandler<T>.onSuccess(action: (T) -> Unit): ResultHandler<T> {
    if (this is SuccessResponse) action(data)
    return this
}

inline fun <T : Any> ResultHandler<T>.onError(action: ErrorResponse.() -> Unit): ResultHandler<T> {
    if (this is ErrorResponse) action(this)
    return this
}

suspend fun <T> resultHandlerOf(action: suspend () -> T) =
    try {
        SuccessResponse(action())
    } catch (e: Exception) {
        ErrorResponse(Failure.analyzeException(e))
    }
