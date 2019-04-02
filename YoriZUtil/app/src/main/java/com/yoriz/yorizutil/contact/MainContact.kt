package com.yoriz.yorizutil.contact

import com.yoriz.yorizutil.mvp.BasePresenter
import com.yoriz.yorizutil.mvp.YoriListener

/**
 * Created by yoriz
 * on 2019/4/2 2:50 PM.
 */
object MainContact{
    interface IMainModel{
        fun getData(listener: YoriListener)
    }

    interface IMainView{
        fun showData(msg:List<String>)
    }

    interface IMainPresenter:BasePresenter{
        fun startGetData()
    }
}