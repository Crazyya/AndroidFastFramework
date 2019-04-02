package com.yoriz.yorizutil.presenter

import com.yoriz.yorizutil.contact.MainContact
import com.yoriz.yorizutil.model.MainModel
import com.yoriz.yorizutil.mvp.YoriListener

/**
 * Created by yoriz
 * on 2019/4/2 2:48 PM.
 */
class MainPresenter(private var view: MainContact.IMainView?) : MainContact.IMainPresenter {
    private val model: MainContact.IMainModel = MainModel()

    override fun onDestroy() {
        view = null
    }

    override fun startGetData() {
        model.getData(object : YoriListener {
            override fun listenerSuccess(data: Any) {
                if (data is List<*> && data.isNotEmpty() && data[0] is String) {
                    view?.showData(data as List<String>)
                }
            }

            override fun listenerFailed(data: Any) {
                view?.showData(ArrayList())
            }
        })
    }

}