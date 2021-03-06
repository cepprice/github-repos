package ru.cepprice.githubprojects.utils

sealed class Resource<T>(
    val data: T?,
    val header: String? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T, header: String? = null) : Resource<T>(data, header)
    class NoContent<T> : Resource<T>(data = null)
    class Error<T>(errorMessage: String?, data: T? = null) : Resource<T>(data, null, errorMessage)
}