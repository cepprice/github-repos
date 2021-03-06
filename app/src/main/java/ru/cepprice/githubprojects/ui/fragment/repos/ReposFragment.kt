package ru.cepprice.githubprojects.ui.fragment.repos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.databinding.FragmentReposBinding
import ru.cepprice.githubprojects.extensions.fromReposListFragmentToAddDialog
import ru.cepprice.githubprojects.extensions.fromReposListFragmentToAuthFragment
import ru.cepprice.githubprojects.extensions.fromReposListFragmentToDeleteDialog
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
        binding = FragmentReposBinding.inflate(inflater, container, false)
        requireActivity().setActionBar(binding.toolbar)
        binding.toolbar.title = ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
        binding.ivLogo.animation = animation

        viewModel.start(args.accessToken)
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

            binding.fab.visibility = View.VISIBLE

            if (viewsResource.data!!.isNullOrEmpty()) {
                showNoReposMessage()
                return@observe
            }

            showRv()
            adapter.setRepos(viewsResource.data)
        })

        viewModel.user.observe(viewLifecycleOwner, { userLoginResource ->
            if (userLoginResource is Resource.Error) {
                return@observe
            }

            val userLogin = userLoginResource.data!!.login
            binding.toolbar.setNavigationIcon(R.drawable.ic_log_out)
            binding.toolbar.title = userLogin
            binding.toolbar.setNavigationOnClickListener {
                findNavController().fromReposListFragmentToAuthFragment()
            }

            adapter.addListener { repo ->
                findNavController().fromReposListFragmentToDeleteDialog(
                    args.accessToken,
                    userLogin,
                    repo
                )
                true
            }

            binding.fab.setOnClickListener {
                findNavController()
                    .fromReposListFragmentToAddDialog(args.accessToken, userLogin)
            }
        })

        findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<String>("DELETED_REPO")
            ?.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.deleteRepo(it)
                }
            })

        findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<RepoView>("CREATED_REPO")
            ?.observe(viewLifecycleOwner, {
                adapter.addRepo(it)
            })
    }

    private fun showRv() {
        hideLogo()
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
        hideLogo()
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
        hideLogo()
        hideRv()
        hideNoReposMessage()
        binding.ivError.visibility = View.VISIBLE
        binding.tvError.visibility = View.VISIBLE
    }

    private fun hideLogo() {
        binding.ivLogo.clearAnimation()
        binding.ivLogo.visibility = View.GONE
    }


}