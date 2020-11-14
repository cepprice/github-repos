package ru.cepprice.githubprojects.extensions

import androidx.navigation.NavController
import ru.cepprice.githubprojects.ui.fragment.auth.AuthFragmentDirections
import ru.cepprice.githubprojects.ui.fragment.repos.ReposFragmentDirections

fun NavController.fromAuthFragmentToReposListFragment(accessToken: String) {
    this.navigate(
        AuthFragmentDirections
            .actionAuthFragmentToReposListFragment(accessToken)
    )
}

fun NavController.fromReposListFragmentToDeleteDialog(accessToken: String, owner: String, repo: String) {
    this.navigate(ReposFragmentDirections
        .actionReposListFragmentToDeleteDialog(accessToken, owner, repo))
}

fun NavController.fromReposListFragmentToAddDialog(accessToken: String, owner: String) {
    this.navigate(ReposFragmentDirections
        .actionReposListFragmentToAddDialog(accessToken, owner))
}