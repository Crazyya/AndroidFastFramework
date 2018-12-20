package com.yoriz.yorizframework.model

import com.yoriz.yorizframework.contact.MainContact
import com.yoriz.yorizframework.util.mvp.MVPListener

/**
 * Created by yoriz
 * on 2018/12/18 5:15 PM.
 */
class MainModel:MainContact.IMainModel{
    override fun getMsg(listener: MVPListener) {
        listener.listenerSuccess("这是YoriZ的MVP快速架构")
    }

}