package ru.cepprice.githubprojects.ui.fragment.delete

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.databinding.DialogDeleteBinding
import ru.cepprice.githubprojects.extensions.getHtmlSpannedString
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.autoCleared

@AndroidEntryPoint
class DeleteDialog
    : BottomSheetDialogFragment(), View.OnClickListener {

    private val args: DeleteDialogArgs by navArgs()
    private lateinit var fullRepoName: String

    private val viewModel: DeleteViewModel by viewModels()

    private var binding: DialogDeleteBinding by autoCleared()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDeleteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullRepoName = "${args.owner}/${args.repo}"

        binding.tvExplanation.text = resources.getHtmlSpannedString(
            R.string.message_explanation, fullRepoName
        )
        binding.tvConfirm.text = resources.getHtmlSpannedString(
            R.string.message_confirm, fullRepoName
        )

        binding.etFullRepoName.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.etFullRepoName.addTextChangedListener(getTextWatcher())

        binding.flIvCancel.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.deletionResult.observe(viewLifecycleOwner, { resource ->
            if (resource is Resource.Error) {
                Log.d("M_DeleteDialog", "Resource has error: ${resource.errorMessage}")
                Toast.makeText(requireContext(), "Error. Try again later", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@observe
            }
            if (resource is Resource.NoContent) {
                Log.d("M_DeleteDialog", "Successful deletion")
                findNavController()
                    .previousBackStackEntry?.savedStateHandle?.set("DELETED_REPO", args.repo)
                findNavController().navigateUp()
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnDelete.id -> {
                viewModel.start(Triple(args.accessToken, args.owner, args.repo))
                binding.etFullRepoName.removeTextChangedListener(getTextWatcher())
                makeBtnInactive()
            }
            binding.flIvCancel.id ->
                findNavController().navigateUp()
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isFullRepoName = p0?.toString().equals(fullRepoName)
                if (isFullRepoName) makeBtnActive()
                else makeBtnInactive()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
    }

    private fun makeBtnActive() {
        with(binding.btnDelete) {
            alpha = 1f
            isClickable = true
        }
    }

    private fun makeBtnInactive() {
        with(binding.btnDelete) {
            alpha = 0.5f
            isClickable = false
        }

    }
}