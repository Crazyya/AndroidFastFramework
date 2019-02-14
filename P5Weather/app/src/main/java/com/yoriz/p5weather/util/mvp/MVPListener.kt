package com.yoriz.yorizframework.util.mvp

/**
 * Created by yoriz
 * on 2018/12/18 5:16 PM.
 * mvp专用接口
 */
interface MVPListener {
    fun listenerSuccess(data: Any)
    fun listenerFailed(data: Any)
    fun listenerOther(data: Any) {}
}