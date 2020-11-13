package ru.cepprice.githubprojects.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.data.remote.RetrofitBuilder
import ru.cepprice.githubprojects.data.repository.Repository

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val token = "token eaf858ed21ae0696301f889e1c7a51f7208d1cfb"
        val url = " https://api.github.com/repos/cepprice/test5/contributors".removeSuffix("{/sha}")

        val urls = listOf(url, url)

        val remoteDataSource = GitHubRemoteDataSource(RetrofitBuilder.apiGitHub)
        val repository = Repository(remoteDataSource)

        CoroutineScope(Dispatchers.IO).launch {
            val user = repository.getUser(token)
            withContext(Dispatchers.Main){
                user.observe(this@TestActivity, {
                    Log.d("M_TestActivity", "${it.data?.login}")
                })

            }
        }

    }
}