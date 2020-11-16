package ru.cepprice.githubprojects.data.local.model

import ru.cepprice.githubprojects.R
import java.io.Serializable


data class RepoView private constructor(
    val name: String,
    val branchesCount: String,
    val tagsCount: String,
    val commitsCount: String,
    val watchersCount: Int,
    val starsCount: Int,
    val forksCount: Int,
    val src: Int
) : Serializable {

    companion object{
        @JvmStatic private val serialVersionUID = 1L
    }

    class Builder() {

        private lateinit var name: String
        private var branchesCount: String = "1 branch"
        private var tagsCount: String = "0 tags"
        private var commitsCount: String = "0 commits"
        private var watchersCount: Int = 1
        private var starsCount: Int = 0
        private var forksCount: Int = 0
        private var src: Int = R.drawable.ic_private

        fun name(name: String) = apply { this.name = name }

        fun branchesCount(branchesCount: String) = apply { this.branchesCount = branchesCount }

        fun tagsCount(tagsCount: String) = apply { this.tagsCount = tagsCount }

        fun commitsCount(commitsCount: String) = apply { this.commitsCount = commitsCount }

        fun watchersCount(watchersCount: Int) = apply { this.watchersCount = watchersCount }

        fun starsCount(starsCount: Int) = apply { this.starsCount = starsCount }

        fun forksCount(forksCount: Int) = apply { this.forksCount = forksCount}

        fun src(src: Int) = apply { this.src = src }

        fun build(): RepoView {
            return RepoView(name, branchesCount, tagsCount,
                commitsCount, watchersCount, starsCount, forksCount, src)
        }
    }
}