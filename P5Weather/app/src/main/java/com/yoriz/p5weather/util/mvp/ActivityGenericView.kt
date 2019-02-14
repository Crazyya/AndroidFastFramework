package com.yoriz.yorizframework.util.mvp

import com.yoriz.yorizframework.util.ToastUtil

/**
 * Created by yoriz
 * on 2018/12/18 11:43 AM.
 * Activity通用接口，有用到就传到presenter内，没有就不管(很愚蠢但我想不到办法分离
 */
interface ActivityGenericView {

    /**
     * 请求成功
     */
    fun <T> requestSuccess(responseData: T) {}

    /**
     * 请求失败
     */
    fun <T> requestFail(responseData:T){}

    /**
     * 调用toast工具弹提示
     */
    fun showPrompt(msg: String) {
        ToastUtil.showShortToast(msg)
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