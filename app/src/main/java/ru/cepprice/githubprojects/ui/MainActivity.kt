package ru.cepprice.githubprojects

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import ru.cepprice.githubprojects.api.AccessToken
import ru.cepprice.githubprojects.api.GitHibApi
import ru.cepprice.githubprojects.api.RepoJson

const val API_BASE_URL = "https://api.github.com"
const val GIT_BASE_URL = "https://github.com"
const val REDIRECT_URI = "cepprice://callback"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
            "https://github.com/login/oauth/authorize/" +
                    "?client_id=${BuildConfig.CLIENT_ID}" +
                    "&scope=repo%20delete_repo" +
                    "&redirect_url=$REDIRECT_URI"
        ))
        startActivity(intent)
        //getData()
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        if (uri != null
            && uri.toString().startsWith(REDIRECT_URI)
            && uri.toString().contains("code=")) {
            val code = uri.getQueryParameter("code")!!

            val gitHibApi = Retrofit.Builder()
                .baseUrl(GIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHibApi::class.java)

            val accessToken = gitHibApi.getAccessToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET,
                code)

            accessToken.enqueue(object : Callback<AccessToken> {
                override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                    Toast.makeText(this@MainActivity, "yes!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "no!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getData() {
        val api = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHibApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getRepos().awaitResponse()
            if (response.isSuccessful) {
                val data: List<RepoJson>? = response.body()
                data?.forEach { Log.d("M_MainActivity", it.name) } ?: Log.d("M_MainActivity", "null")
            } else {

                Log.d("M_MainActivity", response.errorBody().toString())
            }
        }
    }

}