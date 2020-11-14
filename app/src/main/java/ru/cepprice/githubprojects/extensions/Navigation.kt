package ru.cepprice.githubprojects.extensions

import androidx.navigation.NavController
import ru.cepprice.githubprojects.ui.fragment.auth.AuthFragmentDirections
import ru.cepprice.githubprojects.ui.fragment.delete.DeleteDialogDirections
import ru.cepprice.githubprojects.ui.fragment.repos.ReposFragmentDirections

fun NavController.fromAuthFragmentToReposListFragment(accessToken: String) {
    this.navigate(
        AuthFragmentDirections
            .actionAuthFragmentToReposListFragment(accessToken, repo = null)
    )
}

fun NavController.fromReposListFragmentToDeleteRepoDialog(accessToken: String, owner: String, repo: String) {
    this.navigate(ReposFragmentDirections
        .actionReposListFragmentToDeleteDialog(accessToken, owner, repo))
}

fun NavController.fromDeleteDialogToReposListFragment(accessToken: String, deletedRepo: String) {
    this.navigate(DeleteDialogDirections
        .actionDeleteDialogToReposListFragment(accessToken, deletedRepo))
}