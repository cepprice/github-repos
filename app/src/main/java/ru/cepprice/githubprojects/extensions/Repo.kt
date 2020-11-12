package ru.cepprice.githubprojects.extensions

import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.data.remote.model.repo.Repo

fun Repo.toRepoView(
    branchesCount: Int,
    tagsCount: Int,
    commitsCount: Int
): RepoView {

    var branchesString = roundBigCount(branchesCount)
    branchesString += if (branchesCount == 1) " branch"
        else " branches"
    var tagsString = roundBigCount(tagsCount)
    tagsString += if (tagsCount == 1) " tag"
        else " tags"
    var commitsString = decorateCommitsCount(commitsCount)
    commitsString += if (commitsCount == 1) " commit"
        else " commits"

    val src = if (fork) R.drawable.ic_fork else {
        if (private) R.drawable.ic_private
        else R.drawable.ic_public
    }

    return RepoView.Builder()
        .name(name)
        .branchesCount(branchesString)
        .tagsCount(tagsString)
        .commitsCount(commitsString)
        .watchersCount(watchers_count)
        .forksCount(forks_count)
        .starsCount(stargazers_count)
        .src(src)
        .build()
}

private fun roundBigCount(count: Int) =
    when(count) {
        in 0..999 -> "$count"
        in 1000..9999 -> "${count.div(1000)}.${count.rem(1000)}k"
        else -> "${count.div(1000)}k"
    }

private fun decorateCommitsCount(count: Int) =
    if (count >=  1000) "${count.div(1000)},${count.rem(1000)}"
    else "$count"

