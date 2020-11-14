package ru.cepprice.githubprojects.ui.fragment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.cepprice.githubprojects.databinding.DialogAddBinding
import ru.cepprice.githubprojects.utils.autoCleared

class AddDialog : BottomSheetDialogFragment() {

    private var binding: DialogAddBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddBinding.inflate(inflater, container, false)
        return binding.root
    }
}