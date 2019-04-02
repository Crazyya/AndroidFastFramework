package com.yoriz.camerademo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by yoriz
 * on 2019/3/18 10:03 AM.
 */
class CameraUtil private constructor(
        private var context: WeakReference<Activity>?,
        private var surfaceView: SurfaceView,
        /**
         * 是否自动去获取权限
         */
        private var mAutoPermission: Boolean
) : SurfaceHolder.Callback {
    private var mCameraDevice: CameraDevice? = null
    private var mCameraHandler: Handler? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var mCameraCharacteristicsId: Int = CameraCharacteristics.LENS_FACING_BACK
    private var mCameraUtilCallback: CameraUtilCallback? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mImageReader: ImageReader? = null
    private var captureWidth = 0
    private var captureHeight = 0
    private var orientation = 0

    private val ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }


    // 打开camera的回调，在此处赋值 mCameraDevice ,并开启预览
    private val mDeviceCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraDevice?.close()
            mCameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            mCameraDevice?.close()
            mCameraDevice = null
        }

    }

    /**
     * 需要拍照后预览就实现此接口
     */
    interface CameraUtilCallback {
        /**
         * 设置拍照后的size，如果为空默认大小为预览大小
         */
        fun setCaptureSize(): Size?

        fun getBitmap(bitmap: Bitmap)
    }


    /**
     * 预览的尺寸
     */
    private lateinit var mPreviewSize: Size

    companion object {
        /**
         * 请求权限用的requestCode，在activity中的onActivityResult中确认获得权限后调用initCamera方法
         */
        const val PERMISSION_REQUEST_CODE = 528

        /**
         * 获得CameraUtil的实例
         * 默认不自动去获取权限
         */
        fun Build(context: Activity, surfaceView: SurfaceView): CameraUtil {
            return CameraUtil(WeakReference(context), surfaceView, false)
        }

        /**
         * 获得CameraUtil的实例
         *
         */
        fun Build(context: Activity, surfaceView: SurfaceView, mAutoPermission: Boolean): CameraUtil {
            return CameraUtil(WeakReference(context), surfaceView, mAutoPermission)
        }
    }

    /**
     * 开始运行
     */
    fun start() {
        surfaceHolder = surfaceView.holder.apply {
            setKeepScreenOn(true)
            addCallback(this@CameraUtil)
        }

    }

    /**
     * 拍照
     */
    fun capture() {
        if (mCameraDevice == null) return
        val captureRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                        .apply {
                            addTarget(mImageReader!!.surface)
                            set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                            set(
                                    CaptureRequest.JPEG_ORIENTATION, orientation
//                        ORIENTATIONS[context!!.get()!!.windowManager.defaultDisplay.rotation]
                            )
                        }
        val captureRequest = captureRequestBuilder.build()
        mCameraCaptureSession!!.capture(captureRequest, null, mCameraHandler)
    }

    /**
     * 初始化Camera
     */
    private fun initCamera() {
        //先监测是否拥有Camera权限，如果没有的话会根据 mAutoPermission 自动去获取权限
        if (ContextCompat.checkSelfPermission(
                        context?.get()!!,
                        Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (mAutoPermission && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                context?.get()?.requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
                return
            }
        }
        //初始化camera动作的线程
        val mCameraThread = HandlerThread("Camera2").apply {
            start()
        }
        mCameraHandler = Handler(mCameraThread.looper)
        val mainHandler = Handler(Looper.getMainLooper())



        mImageReader = mCameraUtilCallback?.setCaptureSize().let { size ->
            if (size == null) {
                captureHeight = surfaceView.height
                captureWidth = surfaceView.width
            } else {
                captureHeight = size.height
                captureWidth = size.width
            }
            ImageReader.newInstance(captureWidth, captureHeight, ImageFormat.JPEG, 1).apply {
                setOnImageAvailableListener({
                    val image = it.acquireNextImage()
                    val buffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)
                    val bitmap = convert(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                    mCameraUtilCallback!!.getBitmap(bitmap)
                    image.close()
                }, mainHandler)
            }
        }

        val mCameraManager = context?.get()?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var mCameraId: String? = null
        for (cameraId in mCameraManager.cameraIdList) {
            //查询摄像头属性
            val characteristics = mCameraManager.getCameraCharacteristics(cameraId)
            orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == mCameraCharacteristicsId) {
                mCameraId = cameraId
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                mPreviewSize = getOptimalSize(map!!.getOutputSizes(SurfaceTexture::class.java), surfaceView.width, surfaceView.height)
                surfaceHolder!!.setFixedSize(captureWidth, captureHeight)
            }
        }

        if (mCameraId == null) {
            throw Throwable("${this.javaClass.`package`!!.name} ERROR: can't get cameraId!")
        }

        //开启摄像头
        mCameraManager.openCamera(mCameraId, mDeviceCallback, mCameraHandler)
    }

    /**
     * 开始预览
     */
    private fun takePreview() {
        val previewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder.addTarget(surfaceHolder!!.surface)
        mCameraDevice?.createCaptureSession(
                if (mImageReader == null) Arrays.asList(surfaceHolder!!.surface)
                else Arrays.asList(surfaceHolder!!.surface, mImageReader!!.surface)
                ,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Toast.makeText(context?.get(), "配置摄像头失败", Toast.LENGTH_LONG).show()
                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        if (mCameraDevice == null) return
                        mCameraCaptureSession = session
                        //设置自动对焦
                        with(previewRequestBuilder) {
                            set(
                                    CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                            )
                            set(
                                    CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE,
                                    CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON
                            )
                            set(CaptureRequest.SENSOR_SENSITIVITY, 1600)
                            set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, 0)
                        }
                        val previewRequest = previewRequestBuilder.build()
                        mCameraCaptureSession!!.setRepeatingRequest(previewRequest, null, mCameraHandler)
                    }

                },
                mCameraHandler
        )
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCameraDevice?.close()
        mCameraDevice = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        initCamera()
    }

    /**
     * 设置是否自动获取权限
     * 需要在activity中的onActivityResult中确认获得权限后调用 start 方法
     * @see PERMISSION_REQUEST_CODE onActivityResult中的requestCode
     */
    fun setAutoPermission(isAuto: Boolean) {
        mAutoPermission = isAuto
    }

    /**
     * 设置是前置摄像头还是后置摄像头
     * @param characteristicsId 对应参数为以下
     * @see CameraCharacteristics.LENS_FACING_BACK 后置摄像头
     * @see CameraCharacteristics.LENS_FACING_FRONT 前置摄像头
     * 其他参数自行查看，默认为后置摄像头
     */
    fun setCameraCharacteristicsId(characteristicsId: Int) {
        mCameraCharacteristicsId = characteristicsId
    }

    /**
     * 需要拍照后预览就实现此接口
     */
    fun setCaptureCallback(callback: CameraUtilCallback) {
        mCameraUtilCallback = callback
    }

    /**
     * 在activity生命周期中的onDestroy关闭
     */
    fun onDestroy() {
        context = null
        mImageReader?.close()
        mImageReader = null
        mCameraDevice?.close()
        mCameraDevice = null
        mCameraHandler?.removeCallbacksAndMessages(null)
    }

    /**
     * 获取预览的最适宜的长宽比例
     **/
    private fun getOptimalSize(sizeMap: Array<Size>, width: Int, height: Int): Size {
        val sizeList: List<Size> = ArrayList()
        for (option in sizeMap) {
            if (option.height == 1080) {
                (sizeList as ArrayList).add(option)
                continue
            }
            if (width > height) {
                if (option.width > width && option.height > height) (sizeList as ArrayList).add(option)
            } else {
                if (option.width > height && option.height > width) (sizeList as ArrayList).add(option)
            }
        }
        return if (sizeList.isNotEmpty()) {
            Collections.min(sizeList) { lhs, rhs ->
                java.lang.Long.signum((lhs.width * lhs.height - rhs.width * rhs.height).toLong())
            }
        } else {
            return sizeMap[0]
        }
    }

    /**
     * 镜像反转图片
     */
    private fun convert(bitmap: Bitmap): Bitmap {
        val m = Matrix()
        m.postScale(-1f, 1f)
        m.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }
}