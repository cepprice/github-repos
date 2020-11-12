package ru.cepprice.githubprojects.utils

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