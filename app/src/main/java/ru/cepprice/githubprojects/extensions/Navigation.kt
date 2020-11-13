package ru.cepprice.githubprojects.extensions

import androidx.navigation.NavController
import ru.cepprice.githubprojects.ui.fragment.auth.AuthFragmentDirections
import ru.cepprice.githubprojects.ui.fragment.delete.DeleteDialogDirections
import ru.cepprice.githubprojects.ui.fragment.repos.ReposFragment
import ru.cepprice.githubprojects.ui.fragment.repos.ReposFragmentDirections

fun NavController.navigateToReposListFragment(accessToken: String) {
    this.navigate(AuthFragmentDirections
        .actionAuthFragmentToReposListFragment(accessToken = accessToken))
}

fun NavController.navigateToDeleteRepoDialog(accessToken: String, owner: String, repo: String) {
    this.navigate(ReposFragmentDirections
        .actionReposListFragmentToDeleteDialog(accessToken, owner, repo))
}

fun NavController.navigateToReposListFragment(accessToken: String, hasDeleted: Boolean) {
    this.navigate(DeleteDialogDirections
        .actionDeleteDialogToReposListFragment(accessToken, hasDeleted))
}