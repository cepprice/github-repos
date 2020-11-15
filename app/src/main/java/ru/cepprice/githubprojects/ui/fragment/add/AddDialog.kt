package ru.cepprice.githubprojects.ui.fragment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.cepprice.githubprojects.databinding.DialogAddBinding
import ru.cepprice.githubprojects.utils.autoCleared

class AddDialog : BottomSheetDialogFragment(),
    CompoundButton.OnCheckedChangeListener,
    View.OnClickListener
{

    private var binding: DialogAddBinding by autoCleared()

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

        with(binding) {
            cbReadme.setOnCheckedChangeListener(this@AddDialog)
            cbGitignore.setOnCheckedChangeListener(this@AddDialog)
            cbLicense.setOnCheckedChangeListener(this@AddDialog)

            flIvCancel.setOnClickListener(this@AddDialog)
        }

    }

    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
        setVisibilityOfBranchMessage(isChecked)
    }

    override fun onClick(view: View?) {
        if (binding.flIvCancel == view) {
            findNavController().navigateUp()
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

}