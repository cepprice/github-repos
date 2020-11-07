package ru.cepprice.githubprojects.data.local.model


data class RepoView(
    val name: String,
    val isFork: Boolean,
    val isPrivate: Boolean,
    val branchesUrl: String,
    val tagsUrl: String,
    val commitsUrl: String,
    val watchersCount: String,
    val starsCount: String,
    val forksCount: String
) {

    class Builder() {

        private lateinit var name: String
        private var isFork: Boolean = false
        private var isPrivate: Boolean = false
        private lateinit var branchesUrl: String
        private lateinit var tagsUrl: String
        private lateinit var commitsUrl: String
        private lateinit var watchersCount: String
        private lateinit var starsCount: String
        private lateinit var forksCount: String


        fun name(name: String) = apply { this.name = name }

        fun isFork(isFork: Boolean) = apply { this.isFork = isFork }

        fun isPrivate(isPrivate: Boolean) = apply { this.isPrivate = isPrivate }

        fun branchesUrl(branchesUrl: String) = apply { this.branchesUrl = branchesUrl }

        fun tagsUrl(tagsUrl: String) = apply { this.tagsUrl = tagsUrl }

        fun commitsUrl(commitsUrl: String) = apply { this.commitsUrl = commitsUrl }

        fun watchersCount(watchersCount: String) = apply { this.watchersCount = watchersCount }

        fun starsCount(starsCount: String) = apply { this.starsCount = starsCount }

        fun forksCount(forksCount: String) = apply { this.forksCount = forksCount}

        fun build()
                = RepoView(name, isFork, isPrivate, branchesUrl, tagsUrl, commitsUrl,
            watchersCount, starsCount, forksCount)
    }
}