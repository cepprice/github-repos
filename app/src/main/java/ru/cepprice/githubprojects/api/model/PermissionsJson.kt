package ru.cepprice.githubprojects.api.model

data class PermissionsJson(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)