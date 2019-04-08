package com.yoriz.yorizutil.mvp

import com.yoriz.yorizutil.YoriToast

/**
 * Created by yoriz
 * on 2019/4/8 3:35 PM.
 * View的通用接口方法
 */
interface BaseView {
    /**
     * 请求成功
     */
    fun <T> requestSuccess(responseData: T) {}

    /**
     * 请求失败
     */
    fun <T> requestFail(responseData: T) {}

    /**
     * 调用toast工具弹提示
     */
    fun showPrompt(msg: String) {
        YoriToast.showShortToast(msg)
    }

    /**
     * 显示loading
     */
    fun showLoading() {}

    /**
     * 隐藏loading
     */
    fun hideLoading() {}
}