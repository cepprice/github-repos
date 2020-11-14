package ru.cepprice.githubprojects.data.remote

import retrofit2.Response
import ru.cepprice.githubprojects.utils.Resource

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()

                // If status is 204 No Content (when repo was only created and has no commits)
                if (isNoContentStatus(response)) {
                    return Resource.NoContent()
                }

                // Get amount of pages with per_page=1
                val linkHeader = response.headers()["Link"]
                if (body != null) return Resource.Success(body, linkHeader)
            }
            return error("${response.raw().code()} ${response.raw().message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String) =
        Resource.Error<T>(errorMessage = message)

    private fun <T> isNoContentStatus(response: Response<T>) =
        response.body() == null &&
                response.raw().code() == 204 &&
                response.raw().message() == "No Content"
}