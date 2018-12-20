package com.yoriz.yorizframework.contact

import com.yoriz.yorizframework.util.mvp.BasePresenter
import com.yoriz.yorizframework.util.mvp.MVPListener

/**
 * Created by yoriz
 * on 2018/12/18 5:10 PM.
 * 范例类
 */
object MainContact {
    /**
     * M
     */
    interface IMainModel {
        fun getMsg(listener: MVPListener)
    }

    /**
     * V
     */
    interface IMainView {
        fun showMsg(msg: String)
    }

    /**
     * P
     */
    interface IMainPresenter : BasePresenter {
        fun startGetMsg()
    }
}