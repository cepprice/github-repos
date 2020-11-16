package ru.cepprice.githubprojects.ui.fragment.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.cepprice.githubprojects.R
import ru.cepprice.githubprojects.data.local.model.SendRepo
import ru.cepprice.githubprojects.databinding.DialogAddBinding
import ru.cepprice.githubprojects.extensions.toRepoView
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.Utils
import ru.cepprice.githubprojects.utils.autoCleared
import java.io.Serializable

@AndroidEntryPoint
class AddDialog : BottomSheetDialogFragment(),
    CompoundButton.OnCheckedChangeListener,
    View.OnClickListener
{
    // TODO 1: get list of user repos
    // TODO 2: button inactive until repos' name == 0
    //
    private val args: AddDialogArgs by navArgs()
    private val viewModel: AddViewModel by viewModels()

    private var binding: DialogAddBinding by autoCleared()

    private var repoToSend: SendRepo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.start(args.accessToken)
        setupObservers()

        with(binding) {
            rbPublic.isChecked = true

            // Checkboxes
            cbReadme.setOnCheckedChangeListener(this@AddDialog)
//            cbGitignore.setOnCheckedChangeListener(this@AddDialog)
//            cbLicense.setOnCheckedChangeListener(this@AddDialog)

            // Cross image
            flIvCancel.setOnClickListener(this@AddDialog)

            // Radio Buttons
            rbPublic.setOnClickListener(this@AddDialog)
            ivPublic.setOnClickListener(this@AddDialog)
            tvPublic.setOnClickListener(this@AddDialog)
            tvPublicExplain.setOnClickListener(this@AddDialog)
            rbPrivate.setOnClickListener(this@AddDialog)
            ivPrivate.setOnClickListener(this@AddDialog)
            tvPrivate.setOnClickListener(this@AddDialog)
            tvPrivateExplain.setOnClickListener(this@AddDialog)

            // Add button
            btnCreate.setOnClickListener(this@AddDialog)
        }

    }

    private fun setupObservers() {
        viewModel.repos.observe(viewLifecycleOwner, { resource ->
            if (resource is Resource.Error) {
                Toast.makeText(
                    requireContext(), "Error. Try again later", Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
                return@observe
            }

            binding.etRepoName.addTextChangedListener(getTextWatcher())
        })

//        viewModel.gitignoreTemplates.observe(viewLifecycleOwner, { resource ->
//            if (resource is Resource.Error) {
//                Toast.makeText(
//                    requireContext(), "Error. Try again later", Toast.LENGTH_SHORT
//                ).show()
//                findNavController().navigateUp()
//                return@observe
//            }
//        })

        viewModel.addRepoResult.observe(viewLifecycleOwner, { resource ->
            if (resource is Resource.Error) {
                Log.d("M_AddDialog", "${resource.errorMessage}")
                Toast.makeText(
                    requireContext(), "Error. Try again later", Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
                return@observe
            }

            Log.d("M_AddDialog", "Successful repo creation")
            Toast.makeText(requireContext(), "Repository created", Toast.LENGTH_SHORT).show()
            val isInitializationCompleted = with(repoToSend!!) {
                addReadme || gitignore.isNotEmpty() || license.isNotEmpty()
            }
            val commitsAndBranches = if (isInitializationCompleted) 1 else 0
            val view = resource.data!!
                .toRepoView(commitsAndBranches, 0, commitsAndBranches) as Serializable

            with(findNavController()) {
                previousBackStackEntry?.savedStateHandle?.set("CREATED_REPO", view)
                navigateUp()
            }
        })
    }

    // For checkboxes
    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
        setVisibilityOfBranchMessage(isChecked)
    }

    override fun onClick(view: View?): Unit = with(binding) { when(view) {
        flIvCancel -> findNavController().navigateUp()

        rbPublic, ivPublic, tvPublic, tvPublicExplain -> {
            rbPrivate.isChecked = false
            rbPublic.isChecked = true
        }
        rbPrivate, ivPrivate, tvPrivate, tvPrivateExplain -> {
            rbPublic.isChecked = false
            rbPrivate.isChecked = true
        }

        // TODO add license and gitignore template
        btnCreate -> {
            setButtonClickable(false)
            repoToSend = SendRepo(
                etRepoName.text.toString(),
                rbPrivate.isChecked,
                cbReadme.isChecked
            )
            viewModel.createRepo(repoToSend!!)
        }

        else -> Log.d("M_AddDialog", "Click on ${view?.id} is not handled")
    }}

    // For EditText with repo name
    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val repoName = p0.toString()
            val isNameFree = !viewModel.repos.value!!.data!!.map { it.name }.contains(repoName)

            if (Utils.isRepoNameValid(repoName) && isNameFree) {
                if (!binding.cbLicense.isChecked &&
                            !binding.cbGitignore.isChecked) {
                    setButtonClickable(true)
                    return
                }
                // TODO check license and/or gitignore template selected
            }

            setButtonClickable(false)
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private fun setVisibilityOfBranchMessage(isChecked: Boolean) {
        with(binding) {
            val allUnchecked = !(cbGitignore.isChecked ||
                    cbReadme.isChecked ||
                    cbLicense.isChecked)

            if (isChecked) {
                tvMainMessage.visibility = View.VISIBLE
                tvIvMain.visibility = View.VISIBLE
                tvAsDefBranch.visibility = View.VISIBLE
            } else if (allUnchecked){
                tvMainMessage.visibility = View.GONE
                tvIvMain.visibility = View.GONE
                tvAsDefBranch.visibility = View.GONE
            }
        }
    }

    private fun setButtonClickable(clickable: Boolean) {
        with(binding.btnCreate) {
            if (clickable) {
                isClickable = true
                alpha = 1F
            } else {
                isClickable = false
                alpha = 0.5F
            }
        }
    }
}