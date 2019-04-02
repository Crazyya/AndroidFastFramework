package com.yoriz.camerademo

import android.hardware.Camera

/**
 * Created by yoriz
 * on 2019/3/19 2:51 PM.
 */
object test {
    fun getCloselyPreSize(isPortrait: Boolean, surfaceWidth: Int, surfaceHeight: Int, preSizeList: List<Camera.Size>): Camera.Size? {
        val reqTmpWidth: Int
        val reqTmpHeight: Int
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (isPortrait) {
            reqTmpWidth = surfaceHeight
            reqTmpHeight = surfaceWidth
        } else {
            reqTmpWidth = surfaceWidth
            reqTmpHeight = surfaceHeight
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (size in preSizeList) {
            if (size.width == reqTmpWidth && size.height == reqTmpHeight) {
                return size
            }
        }

        // 得到与传入的宽高比最接近的size
        val reqRatio = reqTmpWidth.toFloat() / reqTmpHeight
        var curRatio: Float
        var deltaRatio: Float
        var deltaRatioMin = java.lang.Float.MAX_VALUE
        var retSize: Camera.Size? = null
        for (size in preSizeList) {
            curRatio = size.width.toFloat() / size.height
            deltaRatio = Math.abs(reqRatio - curRatio)
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio
                retSize = size
            }
        }

        return retSize
    }
}
