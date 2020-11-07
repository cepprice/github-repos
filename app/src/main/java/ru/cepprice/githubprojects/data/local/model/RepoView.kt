package ru.cepprice.githubprojects.data.local.model


data class RepoView private constructor(
    val name: String,
    val isFork: Boolean,
    val isPrivate: Boolean,
    val branchesCount: Int,
    val branchCaption: String,
    val tagsCount: Int,
    val tagCaption: String,
    val commitsCount: Int,
    val commitCaption: String,
    val watchersCount: Int,
    val starsCount: Int,
    val forksCount: Int,
) {

    class Builder() {

        private lateinit var name: String
        private var isFork: Boolean = false
        private var isPrivate: Boolean = false

        private var branchesCount: Int = 1
        private var branchCaption: String = "branch"

        private var tagsCount: Int = 0
        private var tagCaption: String = "tags"

        private var commitsCount: Int = 0
        private var commitCaption: String = "commits"

        private var watchersCount: Int = 1
        private var starsCount: Int = 0
        private var forksCount: Int = 0


        fun name(name: String) = apply { this.name = name }

        fun isFork(isFork: Boolean) = apply { this.isFork = isFork }

        fun isPrivate(isPrivate: Boolean) = apply { this.isPrivate = isPrivate }

        fun branchesCount(branchesCount: Int) = apply { this.branchesCount = branchesCount }

        fun tagsCount(tagsCount: Int) = apply { this.tagsCount = tagsCount }

        fun commitsCount(commitsCount: Int) = apply { this.commitsCount = commitsCount }

        fun watchersCount(watchersCount: Int) = apply { this.watchersCount = watchersCount }

        fun starsCount(starsCount: Int) = apply { this.starsCount = starsCount }

        fun forksCount(forksCount: Int) = apply { this.forksCount = forksCount}

        fun build(): RepoView {
            val branchCaption = if (branchesCount == 1) "branch" else "branches"
            val tagCaption = if (tagsCount == 1) "tag" else "tags"
            val commitCaption = if (commitsCount == 1) "commit" else "commits"

            return RepoView(name, isFork, isPrivate, branchesCount, branchCaption, tagsCount,
                tagCaption, commitsCount, commitCaption, watchersCount, starsCount, forksCount)
        }
    }
}