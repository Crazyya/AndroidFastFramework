package com.yoriz.yorizutil.glide

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yoriz.yorizutil.R

/**
 * Created by yoriz
 * on 2019/2/18 12:31 PM.
 * Glide相关的图片工具类
 */
object GlideUtil {

    //SharedPreferences名
    private const val GLIDE_SP_NAME = "GlideSPName"
    //保存在SharedPreferences中加载图的属性名
    private const val LOADING_IMG_NAME = "GlideLoadingImgName"
    //保存在SharedPreferences中异常图的属性名
    private const val ERROR_IMG_NAME = "ErrorImgName"

    /**
     * 整个应用只需执行一次
     * 写入等待图，如果不写入到时候是默认的
     */
    fun setFinalLoadingImg(context: Context, resource: Int) {
        val sp = context.getSharedPreferences(GLIDE_SP_NAME, Context.MODE_PRIVATE)
        val spEdit = sp.edit()
        spEdit.putInt(LOADING_IMG_NAME, resource)
        spEdit.apply()
    }

    /**
     * 整个应用只需执行一次
     * 写入异常图，如果不写入到时候是默认的
     */
    fun setFinalErrorImg(context: Context, resource: Int) {
        val sp = context.getSharedPreferences(GLIDE_SP_NAME, Context.MODE_PRIVATE)
        val spEdit = sp.edit()
        spEdit.putInt(ERROR_IMG_NAME, resource)
        spEdit.apply()
    }


    /**
     * 检测图片是否完整
     * 如果图片不对获取宽的时候就崩了,比较劣质的判断法
     */
    @JvmStatic
    fun checkImgDamage(img: Bitmap?): Boolean {
        try {
            img!!.width
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * glide的RequestOptions
     */
    private fun getGlideOptions(context: Context): RequestOptions {
        val sp = context.getSharedPreferences(GLIDE_SP_NAME, Context.MODE_PRIVATE)
        val loadImg = sp.getInt(LOADING_IMG_NAME, R.mipmap.loading)
        val errImg = sp.getInt(ERROR_IMG_NAME, R.mipmap.load_error)
        return RequestOptions()
                .placeholder(loadImg)
                .error(errImg)
    }

    /**
     * 加载网络图片
     * 统一说明去掉过场动画是因为显示预载图片后会有缓存，使用过场动画会导致预载图片不消失
     * @param option:可以为空，如过为空就为预设的加载图和异常图
     */
    fun loadImg(context: Context, url: String, img: ImageView, option: RequestOptions?) {
        val requestOptions = option ?: getGlideOptions(context)
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(img)
    }

    /**
     * 加载int类型资源图片
     * @param option:可以为空，如过为空就为预设的加载图和异常图
     */
    fun loadImg(context: Context, url: Int, img: ImageView, option: RequestOptions?) {
        val requestOptions = option ?: getGlideOptions(context)
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(img)
    }

    /**
     * 加载byte图片
     * @param option:可以为空，如过为空就为预设的加载图和异常图
     */
    fun loadImg(context: Context, data: ByteArray, img: ImageView, option: RequestOptions?) {
        val requestOptions = option ?: getGlideOptions(context)
        Glide.with(context)
                .load(data)
                .apply(requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .into(img)
    }

}