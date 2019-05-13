package com.yoriz.yorizutil.mvp

import android.app.Service

/**
 * Created by yoriz
 * on 2019-05-05 09:07.
 * 和mvpActivity一样
 */
abstract class MVPService : Service() {
    private var basePresenter: BasePresenter? = null

    protected abstract fun bindPresenter(): BasePresenter

    override fun onCreate() {
        super.onCreate()
        basePresenter = bindPresenter()
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        basePresenter = null
        super.onDestroy()
    }
}