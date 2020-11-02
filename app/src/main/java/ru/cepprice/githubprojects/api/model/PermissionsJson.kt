package ru.cepprice.githubprojects.api

data class PermissionsJson(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)