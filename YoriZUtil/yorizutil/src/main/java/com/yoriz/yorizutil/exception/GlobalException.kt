package com.yoriz.yorizutil.exception

import android.app.Activity
import android.os.Environment
import com.yoriz.yorizutil.BaseApplication
import com.yoriz.yorizutil.R
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by yoriz
 * on 2018/12/18 2:52 PM.
 *
 * 错误处理类，所有异常信息会写成文本
 */
class GlobalException private constructor() : Thread.UncaughtExceptionHandler {

    private val mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    companion object {
        @JvmStatic
        val crashFileName = "${BaseApplication.instance.getString(R.string.error_text_name)}.txt"

        private var instance: GlobalException? = null

        fun instance(): GlobalException {
            if (instance == null) {
                synchronized(GlobalException::class.java) {
                    if (instance == null) {
                        instance = GlobalException()
                    }
                }
            }
            return instance!!
        }
    }

    override fun uncaughtException(t: Thread?, ex: Throwable?) {
        try {
            if (handleException(ex) && mDefaultHandler != null) {
                //系统默认的异常处理器处理
                mDefaultHandler.uncaughtException(t, ex)
                System.exit(1)
            } else {
                //退出程序
                System.exit(1)
            }
        } catch (e: Exception) {
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, ex)
            } else {
                System.exit(1)
            }
        }
    }

    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) return false
        //写入文件
        saveErrorInfoFile(ex)
        return true
    }

    /**
     * 记录错误信息
     */
    private fun saveErrorInfoFile(ex: Throwable) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            printWriter.close()
            val result = writer.toString()
            val outputStream = BaseApplication.instance.applicationContext.openFileOutput(crashFileName, Activity.MODE_APPEND)
            outputStream.write(result.toByteArray())
            outputStream.flush()
            outputStream.close()
        }
    }
}