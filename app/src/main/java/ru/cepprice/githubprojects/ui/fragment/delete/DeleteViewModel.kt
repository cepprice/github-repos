package ru.cepprice.githubprojects.ui.fragment.delete

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import ru.cepprice.githubprojects.data.repository.Repository
import ru.cepprice.githubprojects.utils.Resource

class DeleteViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    private val deletionParams = MutableLiveData<Triple<String, String, String>>()

    private val mDeletionResult = deletionParams.switchMap { params ->
        with(params){
            repository.deleteRepo(first, second, third)
        }
    }
    val deletionResult: LiveData<Resource<Void>> = mDeletionResult


    fun start(deletionParams: Triple<String, String, String>) {
        this.deletionParams.value = deletionParams
    }
}