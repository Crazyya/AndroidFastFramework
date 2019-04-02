package com.yoriz.yorizutil.exception

import android.app.Activity
import com.yoriz.yorizutil.BaseApplication
import java.io.ByteArrayOutputStream

/**
 * Created by yoriz
 * on 2018/12/18 3:03 PM.
 */
object ErrorDetail {

    /**
     * 获取异常崩溃后保存在txt文件内的数据
     *
     * @return 字符串异常信息
     */
    val errorTxtMessage: String
        get() {
            var result = ""
            try {
                val inputStream = BaseApplication.instance.applicationContext.openFileInput(GlobalException.crashFileName)
                val bytes = ByteArray(1024)
                val arrayOutputStream = ByteArrayOutputStream()
                while (inputStream.read(bytes) != -1) {
                    arrayOutputStream.write(bytes, 0, bytes.size)
                }
                inputStream.close()
                arrayOutputStream.close()
                result = String(arrayOutputStream.toByteArray())
            } catch (ignored: Exception) {
            }

            return result
        }

    /**
     * 清空txt文件内容
     */
    fun clearErrorTxtMessage() {
        try {
            val outputStream = BaseApplication.instance.applicationContext.openFileOutput(GlobalException.crashFileName, Activity.MODE_PRIVATE)
            outputStream.write("".toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (ignored: Exception) {
        }

    }
}
