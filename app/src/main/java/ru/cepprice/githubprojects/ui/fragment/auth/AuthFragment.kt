package ru.cepprice.githubprojects.ui.fragment.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.coroutines.*
import retrofit2.Call
import ru.cepprice.githubprojects.BuildConfig
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.remote.RetrofitBuilder
import ru.cepprice.githubprojects.data.remote.model.AccessToken
import ru.cepprice.githubprojects.data.remote.extensions.enqueue
import ru.cepprice.githubprojects.extensions.fromAuthFragmentToReposListFragment

@AndroidEntryPoint
class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wv_auth.settings.javaScriptEnabled = true
        wv_auth.webViewClient = getWebViewClient()
        wv_auth.loadUrl(buildAuthUrl())
            .also { Log.d("M_AuthFragment", "WebView is loading url: ${buildAuthUrl()}") }

    }

    private fun buildAuthUrl(): String {
        return "https://github.com/login/oauth/authorize/" +
                "?client_id=${BuildConfig.CLIENT_ID}" +
                "&scope=repo%20delete_repo" +
                "&redirect_url=${RetrofitBuilder.REDIRECT_URI}"
    }

    private fun getWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return true
            }

            // To hide error message
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                view?.visibility = View.GONE
            }

            // Don't have to wait until page will be loaded,
            // because this page has no useful info
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("M_AuthFragment", "page loaded with url: $url")
                processRedirectUrl(url)
            }
        }
    }

    private fun processRedirectUrl(url: String?) {
        val isUrlValid = url != null &&
                url.startsWith(RetrofitBuilder.REDIRECT_URI) &&
                url.contains("code=")

        if (isUrlValid) {
            CoroutineScope(Dispatchers.IO).launch {
                val callAccessToken = getCallAccessToken(url)

                callAccessToken.enqueue(
                    success = {
                        val accessToken = it.body()?.accessToken!!
                        Log.d("M_AuthFragment", "Access token: $accessToken")
                        findNavController().fromAuthFragmentToReposListFragment("token $accessToken")
                    },
                    error = {
                        Log.d("M_AuthFragment", "Got error enqueuing")
                    }
                )
            }
        }
    }

    private fun getCallAccessToken(url: String?): Call<AccessToken> {
        val code = url?.substringAfter("code=")!!
        return RetrofitBuilder.authGitHub.getAccessToken(
            BuildConfig.CLIENT_ID,
            BuildConfig.CLIENT_SECRET,
            code
        )

    }

}
