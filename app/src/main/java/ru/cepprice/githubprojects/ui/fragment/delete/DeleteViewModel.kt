package ru.cepprice.githubprojects.ui.fragment.delete

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import kotlinx.coroutines.*
import ru.cepprice.githubprojects.data.repository.Repository

class DeleteViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    private val deletionParams = MutableLiveData<Triple<String, String, String>>()

    val toMakeInactive = MutableLiveData<Boolean>()

    val deletionResult = deletionParams.switchMap { params ->
        with(params){
            repository.deleteRepo(first, second, third)
        }
    }


    fun start(deletionParams: Triple<String, String, String>) {
    }
}