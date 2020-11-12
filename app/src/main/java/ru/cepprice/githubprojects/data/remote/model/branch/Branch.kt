package ru.cepprice.githubprojects.data.remote.model.branch

data class Branch(
    val commit: Commit,
    val name: String,
    val `protected`: Boolean
)