package ru.cepprice.githubprojects.data.remote.model.tag

data class Tag(
    val commit: Commit,
    val name: String,
    val node_id: String,
    val tarball_url: String,
    val zipball_url: String
)