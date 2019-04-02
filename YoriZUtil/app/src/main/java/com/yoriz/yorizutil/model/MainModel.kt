package com.yoriz.yorizutil.model

import com.yoriz.yorizutil.contact.MainContact
import com.yoriz.yorizutil.mvp.YoriListener

/**
 * Created by yoriz
 * on 2019/4/2 2:49 PM.
 */
class MainModel : MainContact.IMainModel {
    override fun getData(listener: YoriListener) {
        val result = ArrayList<String>()
        result.add("显示Dialog")
        listener.listenerSuccess(result)
    }
}