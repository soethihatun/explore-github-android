package co.binary.exploregithubandroid.core.network

import retrofit2.Response

internal suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            Result.failure(Exception())
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
