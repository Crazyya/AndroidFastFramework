package com.yoriz.camerademo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var cameraDevice: CameraDevice? = null
    private lateinit var cameraManager: CameraManager
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var childHandler: Handler
    private lateinit var mainHandler: Handler
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var imageReader: ImageReader

    private val ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            img.visibility = View.GONE
            //开启摄像头
            cameraDevice = camera
            //开启预览
            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            //关闭摄像头
            cameraDevice?.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Toast.makeText(this@MainActivity, "摄像头开启失败", Toast.LENGTH_LONG).show()
        }
    }

    private fun takePreview() {
        val previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder.addTarget(surfaceHolder.surface)
        cameraDevice?.createCaptureSession(
            Arrays.asList(surfaceHolder.surface, imageReader.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toast.makeText(this@MainActivity, "配置摄像头失败", Toast.LENGTH_LONG).show()
                }

                override fun onConfigured(session: CameraCaptureSession) {
                    if (cameraDevice == null) return
                    cameraCaptureSession = session
                    previewRequestBuilder.set(
                        CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )
                    val previewRequest = previewRequestBuilder.build()
                    cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler)
                }

            },
            childHandler
        )
    }

    private fun initCamera() {
        val handlerThread = HandlerThread("Camera2").apply {
            start()
        }
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(Looper.getMainLooper())
        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.YV12, 1).apply {
            setOnImageAvailableListener({
                val image = it.acquireNextImage()
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                img.setImageBitmap(bitmap)
                img.visibility = View.VISIBLE
            }, mainHandler)
        }
        cameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraManager.openCamera(CameraCharacteristics.LENS_FACING_FRONT.toString(), stateCallback, mainHandler)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 100)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surfaceHolder = surface.holder.apply {
            setKeepScreenOn(true)
            addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    initCamera()
                }

            })
        }
        check.setOnClickListener {
            if (cameraDevice == null) return@setOnClickListener
            val captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                    .apply {
                        addTarget(imageReader.surface)
                        set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                        set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[windowManager.defaultDisplay.rotation])
                    }
            val captureRequest = captureRequestBuilder.build()
            cameraCaptureSession.capture(captureRequest, null, childHandler)
        }
        refresh.setOnClickListener {
            initCamera()
        }
    }
}
