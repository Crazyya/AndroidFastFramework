package com.yoriz.yorizframework.util.exception;

import android.app.Activity;

import com.yoriz.yorizframework.util.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by yoriz
 * on 2018/12/18 3:03 PM.
 */
public class ErrorDetail {

    /**
     * 获取异常崩溃后保存在txt文件内的数据
     *
     * @return 字符串异常信息
     */
    public static String getErrorTxtMessage() {
        String result = "";
        try {
            FileInputStream inputStream = MyApplication.instance().getApplicationContext().openFileInput(GlobalException.getCrashFileName());
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            result = new String(arrayOutputStream.toByteArray());
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 清空txt文件内容
     */
    public static void clearErrorTxtMessage() {
        try {
            FileOutputStream outputStream = MyApplication.instance().getApplicationContext().openFileOutput(GlobalException.getCrashFileName(), Activity.MODE_PRIVATE);
            outputStream.write("".getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception ignored) {
        }
    }
}
