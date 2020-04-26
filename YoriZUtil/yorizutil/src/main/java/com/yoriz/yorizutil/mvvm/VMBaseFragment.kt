package com.yoriz.yorizutil.mvvm

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yoriz.yorizutil.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * Created by yoriz
 * on 2019-09-23 12:12.
 */
abstract class VMBaseFragment<VM : BaseViewModel> : CoroutineScope by MainScope(), Fragment() {

    protected abstract fun initViewModel(): VM

    protected val vm: VM by lazy {
        initViewModel()
    }

    /**
     * 布局
     */
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }
}