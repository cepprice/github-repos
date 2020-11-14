package ru.cepprice.githubprojects.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.data.remote.RetrofitBuilder
import ru.cepprice.githubprojects.data.repository.Repository
import ru.cepprice.githubprojects.utils.Resource

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val token = "token f6978b19dec34e703cfca120c9c9f67caf22a413"
        val url = " https://api.github.com/repos/cepprice/private"

        val urls = listOf(url, url)

        val remoteDataSource = GitHubRemoteDataSource(RetrofitBuilder.apiGitHub)
        val repository = Repository(remoteDataSource)

        val resource = repository.deleteRepo(token, "cepprice", "private")
        resource.observe(this, {
            if (it is Resource.Error) {
                Log.d("M_TestActivity", it.errorMessage!!)
            } else {
                Log.d("M_TestActivity", "success; ${it is Resource.NoContent}")
            }
        })
//        CoroutineScope(Dispatchers.IO).launch {
//            val res = remoteDataSource.deleteRepo(token, "cepprice", "private")
//            if (res !is Resource.Error) {
//                Log.d("M_TestActivity", "success; ${res.header}")
//            } else {
//                Log.d("M_TestActivity", "error: ${res.errorMessage}")
//            }
//        }

    }
}