package ru.cepprice.githubprojects.ui.extensions

import androidx.navigation.NavController
import ru.cepprice.githubprojects.ui.fragment.auth.AuthFragmentDirections

fun NavController.navigateToReposListFragment(accessToken: String) {
    this.navigate(AuthFragmentDirections.actionAuthFragmentToReposListFragment(accessToken))
}