package ru.cepprice.githubprojects.data.remote.model

data class PermissionsJson(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)