package com.yoriz.yorizframework.util

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.Toast

/**
 * Created by yoriz
 * on 2018/12/18 11:43 AM.
 */
object ToastUtil {
    private var toast: Toast? = null
    private var handler = Handler()

    @SuppressLint("ShowToast")
    fun showShortToast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.instance(), msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
            toast!!.duration = Toast.LENGTH_SHORT
        }
        handler.post { toast!!.show() }
    }

    @SuppressLint("ShowToast")
    fun showLongToast(msg:String){
        if (toast == null) {
            toast = Toast.makeText(MyApplication.instance(), msg, Toast.LENGTH_LONG)
        } else {
            toast!!.setText(msg)
            toast!!.duration = Toast.LENGTH_SHORT
        }
        handler.post { toast!!.show() }
    }
}