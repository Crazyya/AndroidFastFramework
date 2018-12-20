package com.yoriz.yorizframework.presenter

import android.os.Message
import com.yoriz.yorizframework.contact.MainContact
import com.yoriz.yorizframework.model.MainModel
import com.yoriz.yorizframework.util.mvp.MVPListener

/**
 * Created by yoriz
 * on 2018/12/18 5:15 PM.
 */
class MainPresenter(private var view: MainContact.IMainView?) : MainContact.IMainPresenter {

    private val model: MainContact.IMainModel = MainModel()


    override fun startGetMsg() {
        model.getMsg(object : MVPListener {
            override fun listenerSuccess(data: Any) {
                if (data is Message) {
                    view?.showMsg(data.obj.toString())
                } else {
                    view?.showMsg(data.toString())
                }
            }

            override fun listenerFailed(data: Any) {
                view?.showMsg(data.toString())
            }
        })
    }

    override fun onDestroy() {
        view = null
    }

}