package ru.cepprice.githubprojects.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.data.remote.RetrofitBuilder
import ru.cepprice.githubprojects.data.repository.Repository

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val token = "token fa85917fd1857c172616ae4c0c0e40cfcf033711"
        val url = " https://api.github.com/repos/cepprice/test5/contributors".removeSuffix("{/sha}")

        val urls = listOf(url, url)

        val remoteDataSource = GitHubRemoteDataSource(RetrofitBuilder.apiGitHub)
        val repository = Repository(remoteDataSource)

        CoroutineScope(Dispatchers.IO).launch {
            val contributors = RetrofitBuilder.apiGitHub.getContributors(token, url)
            Log.d("M_TestActivity", "${contributors.code()}")
            Log.d("M_TestActivity", "${contributors.body()}")
        }

    }
}