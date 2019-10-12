package com.yoriz.yorizutil.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by yoriz
 * on 2019-09-23 12:26.
 */
abstract class BaseViewModel : ViewModel() {
    protected val showDialog = MutableLiveData<Boolean>()

    protected val msg = MutableLiveData<Any>()

    protected val error = MutableLiveData<Any>()

    fun getShowDialog(lifecycle: () -> Lifecycle, observe: (Boolean) -> Unit) {
        showDialog.observe(lifecycle, observe)
    }

    fun getError(lifecycle: () -> Lifecycle, observe: (Any) -> Unit) {
        error.observe(lifecycle, observe)
    }

    fun getMsg(lifecycle: () -> Lifecycle, observe: (Any) -> Unit) {
        msg.observe(lifecycle, observe)
    }

    override fun onCleared() {
        super.onCleared()
        showDialog.value = null
        error.value = null
        msg.value = null
    }
}