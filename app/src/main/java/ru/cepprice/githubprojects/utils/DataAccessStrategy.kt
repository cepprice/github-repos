package ru.cepprice.githubprojects.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

fun <T> performGetOperation(call: suspend () -> Resource<T>):
        LiveData<Resource<T>> = liveData(Dispatchers.IO){
    val resourceStatus = call.invoke()
    this.emit(resourceStatus)
}

fun <T> performPostOperation(call: suspend () -> Resource<T>):
        LiveData<Resource<T>> = liveData(Dispatchers.IO) {
    val responseStatus = call.invoke()
    this.emit(responseStatus)
}

fun <T> performDeleteOperation(call: suspend () -> Resource<T>):
        LiveData<Resource<T>> = liveData(Dispatchers.IO) {
    val responseStatus = call.invoke()
    this.emit(responseStatus)
}
