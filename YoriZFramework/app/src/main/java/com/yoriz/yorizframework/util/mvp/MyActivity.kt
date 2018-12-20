package com.yoriz.yorizframework.util.mvp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by yoriz
 * on 2018/12/18 12:25 PM.
 */
abstract class MyActivity : AppCompatActivity(), ActivityGenericView {

    companion object {
        // 应用退出用的广播标签
        private const val EXIT_APP_ACTION = "finish"
    }

    /**
     * 退出应用使用的广播
     */
    private val exitBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == EXIT_APP_ACTION && context is MyActivity) {
                context.finish()
            }
        }
    }

    // 使用户不能超速进入返回，跳转动画没做完就返回次数多了视觉上会觉得卡的
    private var isBack = false

    /**
     * 此处是最下方虚拟按键的背景色
     */
    protected var footNavigationBarColor = android.R.color.black

    /**
     * 布局
     */
    protected abstract val layoutId: Int

    /**
     * 初始数据
     */
    protected abstract fun initData()

    /**
     * 初始化view的各项
     */
    protected abstract fun initView()

    /**
     * 初始化监听，因为kotlin直接使用控件名因此不使用继承监听的方式
     */
    protected abstract fun initClick()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.navigationBarColor = ContextCompat.getColor(this, footNavigationBarColor)
        super.onCreate(savedInstanceState)
        //获得布局
        setContentView(layoutId)
        val intentFilter = IntentFilter()
        intentFilter.addAction(EXIT_APP_ACTION)
        registerReceiver(exitBroadcastReceiver, intentFilter)
        initView()
        initClick()
        initData()
    }

    override fun onStart() {
        super.onStart()
        // 每次进入应用都有0.8s时间后才允许退出
        isBack = false
        Thread(Runnable {
            Thread.sleep(800)
            isBack = true
        }).start()
        super.onStart()
    }

    override fun onBackPressed() {
        if (isBack) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        // 释放广播监听
        unregisterReceiver(exitBroadcastReceiver)
        super.onDestroy()
    }

    /**
     * 退出应用
     */
    protected fun exitAPP() {
        try {
            val intent = Intent()
            intent.action = EXIT_APP_ACTION
            sendBroadcast(intent)
            ActivityCompat.finishAfterTransition(this)
        } catch (e: Exception) {
            finish()
        }
    }
}