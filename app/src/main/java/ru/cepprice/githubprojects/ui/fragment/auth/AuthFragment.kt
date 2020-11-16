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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.cepprice.githubprojects.BuildConfig
import ru.cepprice.githubprojects.databinding.FragmentAuthBinding
import ru.cepprice.githubprojects.extensions.fromAuthFragmentToReposListFragment
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.Utils
import ru.cepprice.githubprojects.utils.autoCleared

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val args: AuthFragmentArgs by navArgs()
    private var binding: FragmentAuthBinding by autoCleared()

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.clearCache) {
            TODO("need to clear cache and login to GitHub again")
        }

        binding.wvAuth.settings.javaScriptEnabled = true
        binding.wvAuth.webViewClient = getWebViewClient()
        binding.wvAuth.loadUrl(buildAuthUrl())
            .also { Log.d("M_AuthFragment", "WebView is loading url: ${buildAuthUrl()}") }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.accessToken.observe(viewLifecycleOwner, { resource ->
            if (resource is Resource.Error) {
                Log.d("M_AuthFragment", "Error: cannot get access token")
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                return@observe
            }

            val accessToken = "token ${resource.data!!.accessToken}"
            findNavController()
                .fromAuthFragmentToReposListFragment(accessToken)
                .also { Log.d("M_AuthFragment", "Pass access token: $accessToken") }
        })
    }

    private fun buildAuthUrl(): String {
        return "https://github.com/login/oauth/authorize/" +
                "?client_id=${BuildConfig.CLIENT_ID}" +
                "&scope=repo%20delete_repo" +
                "&redirect_url=${BuildConfig.REDIRECT_URL}"
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
                binding.wvAuth.visibility = View.GONE
                binding.ivLogo.visibility = View.VISIBLE
            }

            // Don't have to wait until page will be loaded,
            // because this page has no useful info
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("M_AuthFragment", "page loaded with url: $url")

                val code = Utils.getCodeFromRedirectUri(url)
                if (code == null) {
                    Log.d("M_AuthFragment", "Got invalid code")
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    return
                }

                viewModel.start(TokenQueryParams(
                    BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, code
                ))
            }
        }
    }
}
