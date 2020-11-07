package ru.cepprice.githubprojects.data.local.model


data class RepoView(
    val name: String,
    val branches: Int,
    val tags: Int,
    val watch: String,
    val star: String,
    val fork: String
) {

    class Builder() {
        private var
    }
}