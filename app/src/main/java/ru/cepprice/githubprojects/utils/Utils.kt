package ru.cepprice.githubprojects.utils

import ru.cepprice.githubprojects.BuildConfig


object Utils {
    fun parseHeader(header: String?): Int? {
        if (header == null) return null

        val pattern = "<https:\\/\\/api\\.github\\.com\\/repositories" +
                "\\/[0-9]+\\/[a-z]+\\?per_page=1&page=[0-9]+>; rel=\"last\""
        val regex = Regex(pattern)
        if (!regex.containsMatchIn(header)) return null

        val query = "page="
        val start = header.lastIndexOf(query) + query.length
        val end = header.lastIndexOf(">;")

        val s = header[start]
        val e = header[end]

        return header.substring(start until end).toInt()
    }

    fun isRepoNameValid(name: String): Boolean {
        val pattern = "[a-zA-Z0-9_-]+"
        val regex = Regex(pattern)

        return regex.matches(name)
    }

    fun getCodeFromRedirectUri(uri: String?): String? {
        if (uri == null || !uri.startsWith(BuildConfig.REDIRECT_URL) ||
            !uri.contains("code=")) return null
        return uri.substringAfter("code=")
    }
}