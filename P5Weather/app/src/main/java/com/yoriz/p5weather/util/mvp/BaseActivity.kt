package com.yoriz.yorizframework.util.mvp

import android.os.Bundle

/**
 * Created by yoriz
 * on 2018/12/18 12:22 PM.
 * 只在使用MVP的页面使用此Activity，如不使用MVP则使用MyActivity
 */
abstract class BaseActivity : MyActivity() {
    private var basePresenter: BasePresenter? = null
    protected abstract fun bindPresenter(): BasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        basePresenter = bindPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (basePresenter != null) {
            basePresenter!!.onDestroy()
            basePresenter = null
        }
    }
}