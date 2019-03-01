package com.yoriz.yorizutil

import android.app.Application
import android.os.Build
import com.yoriz.yorizutil.exception.GlobalException

/**
 * Created by yoriz
 * on 2018/12/18 11:45 AM.
 */
class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        /**
         * 对外开放的application对象
         */
        @JvmStatic
        fun instance() = instance

        /**
         * 获得应用版本号
         */
        @JvmStatic
        fun getVersionCode(): Long {
            val manager = instance.packageManager
            val info = manager.getPackageInfo(instance.packageName, 0)
            // 9.0开始使用longVersionCode，因此要在此处判断版本
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        //全局错误信息收集
        Thread.setDefaultUncaughtExceptionHandler(GlobalException.instance())
    }
}