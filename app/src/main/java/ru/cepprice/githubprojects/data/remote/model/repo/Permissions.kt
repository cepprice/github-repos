package ru.cepprice.githubprojects.data.remote.model.repo

data class Permissions(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)