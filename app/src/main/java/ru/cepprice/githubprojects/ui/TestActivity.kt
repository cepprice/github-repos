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

        val token = "token cc4eae6685cff636fda59a596abf2170a392d36c"
        val url = " https://api.github.com/repos/cepprice/json-parse/branches?per_page=1".removeSuffix("{/sha}")

        val urls = listOf(url, url)

        val remoteDataSource = GitHubRemoteDataSource(RetrofitBuilder.apiGitHub)
        val repository = Repository(remoteDataSource)

        CoroutineScope(Dispatchers.IO).launch {
            val branches = RetrofitBuilder.apiGitHub.getBranches(token, url)
            Log.d("M_TestActivity", "${branches.code()} ${branches.headers()["Link"]}")
        }

    }
}