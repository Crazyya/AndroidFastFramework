package com.yoriz.camerademo

import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.util.Size
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by yoriz
 * on 2019/3/18 5:54 PM.
 */
class SecondActivity : AppCompatActivity() {
    private lateinit var camera: CameraUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        camera = CameraUtil.Build(this, surface)
        camera.setCameraCharacteristicsId(CameraCharacteristics.LENS_FACING_FRONT)
        camera.setCaptureCallback(object : CameraUtil.CameraUtilCallback {
            override fun setCaptureSize(): Size? {
                return null
            }

            override fun getBitmap(bitmap: Bitmap) {
                img.setImageBitmap(bitmap)
                img.visibility = View.VISIBLE
            }

        })
        camera.start()
        check.setOnClickListener {
            camera.capture()
        }
        refresh.setOnClickListener {
            img.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        camera.onDestroy()
        super.onDestroy()
    }
}