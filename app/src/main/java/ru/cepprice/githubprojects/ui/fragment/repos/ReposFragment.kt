package ru.cepprice.githubprojects.ui.fragment.repos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.cepprice.githubprojects.databinding.FragmentReposBinding
import ru.cepprice.githubprojects.extensions.fromReposListFragmentToDeleteRepoDialog
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.autoCleared

@AndroidEntryPoint
class ReposFragment : Fragment() {

    private val args: ReposFragmentArgs by navArgs()

    private var binding: FragmentReposBinding by autoCleared()
    private val viewModel: ReposViewModel by viewModels()
    private lateinit var adapter: ReposAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("M_ReposFragment", "onCreateView")
        binding = FragmentReposBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("M_ReposFragment", "onViewCreated")
        viewModel.start(args.accessToken!!)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = ReposAdapter()
        binding.rvRepos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRepos.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.repoViewsLd.observe(viewLifecycleOwner, { viewsResource ->
            if (viewsResource is Resource.Error) {
                showErrorMessage()
                Log.d("M_ReposFragment", "repos: ${viewsResource.errorMessage}")
                return@observe
            }

            if (viewsResource.data!!.isNullOrEmpty()) {
                showNoReposMessage()
                return@observe
            }

            showRv()
            adapter.setRepos(viewsResource.data)
        })

        viewModel.user.observe(viewLifecycleOwner, { userLoginResource ->
            if (userLoginResource is Resource.Error) {
                Log.d("M_ReposFragment", "get login error: ${userLoginResource.errorMessage}")
                return@observe
            }

            adapter.addListener { repo ->
                findNavController().fromReposListFragmentToDeleteRepoDialog(
                    args.accessToken!!,
                    userLoginResource.data!!.login,
                    repo
                )
                true
            }
        })

        findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<String>("DELETED_REPO")
            ?.observe(viewLifecycleOwner, {
                Log.d("M_ReposFragment", "change: $it")
                if (it != null) {
                    adapter.deleteRepo(it).also { Log.d("M_ReposFragment", "got $it") }
                }
            })
    }

    private fun showRv() {
        hideProgressBar()
        hideErrorMessage()
        hideNoReposMessage()
    }


    private fun hideNoReposMessage() {
        binding.tvNoRepos.visibility = View.GONE
        binding.ivNoRepos.visibility = View.GONE
    }

    private fun showNoReposMessage() {
        hideRv()
        hideErrorMessage()
        hideProgressBar()
        binding.ivNoRepos.visibility = View.VISIBLE
        binding.tvNoRepos.visibility = View.VISIBLE

    }

    private fun hideRv() {
        binding.rvRepos.visibility = View.GONE
    }

    private fun hideErrorMessage() {
        binding.tvError.visibility = View.GONE
        binding.ivError.visibility = View.GONE
    }

    private fun showErrorMessage() {
        hideProgressBar()
        hideRv()
        hideNoReposMessage()
        binding.ivError.visibility = View.VISIBLE
        binding.tvError.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pb.visibility = View.GONE
    }

}