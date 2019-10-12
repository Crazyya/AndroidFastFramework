package com.yoriz.yorizdemo

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView


/**
 * Created by yoriz
 * on 2019-05-20 10:09.
 */
class DimmedLayerView : View {

    companion object {
        private const val WIDTH_RATIO = 16
        private const val HEIGHT_RATIO = 9
        private const val DIMMED_ALPHA = 100
        private val DIMMED_COLOR = Color.parseColor("#5fC0C0C0")
    }

    private lateinit var mCenterRectPath: Path
    private lateinit var mDimmedPath: Path
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xFerMode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var w = 0f
    private var h = 0f
    //中间抠图层的宽高
    private var mCenterWidth = 0f
    private var mCenterHeight = 0f

    //中间抠图层的坐标，可以结合显示图片的view进行剪裁
    var mCenterRect = RectF()
        private set

    //默认的宽高比
    private var widthRatio = WIDTH_RATIO
        private set(value) {
            field = if (value == 0) WIDTH_RATIO
            else value
        }

    private var heightRatio = HEIGHT_RATIO
        private set(value) {
            field = if (value == 0) HEIGHT_RATIO
            else value
        }

    /**
     * 设置比例
     */
    fun setRatio(widthRatio: Int, heightRatio: Int) {
        this.widthRatio = widthRatio
        this.heightRatio = heightRatio
        invalidate()
    }

    /**
     * 阴影颜色
     */
    var dimmedColor = DIMMED_COLOR
        set(value) {
            field = value
            mPaint.color = field
            mPaint.alpha = dimmedAlpha
            invalidate()
        }

    /**
     * 阴影透明度
     */
    var dimmedAlpha = DIMMED_ALPHA
        set(value) {
            field = if (value > 255) DIMMED_ALPHA
            else value
            mPaint.alpha = field
            invalidate()
        }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        init()
    }

    private fun init() {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        mCenterRectPath = Path()

        mDimmedPath = Path()
        mPaint.color = dimmedColor
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.alpha = dimmedAlpha
    }

    private fun initAttrs(attrs: AttributeSet?) {
        if (attrs == null) return
        context.obtainStyledAttributes(attrs, R.styleable.DimmedLayerView).run {
            widthRatio = getInt(R.styleable.DimmedLayerView_width_ratio, WIDTH_RATIO)
            heightRatio = getInt(R.styleable.DimmedLayerView_height_ratio, HEIGHT_RATIO)
            dimmedColor = getColor(R.styleable.DimmedLayerView_dimmed_color, DIMMED_COLOR)
            dimmedAlpha = getInt(R.styleable.DimmedLayerView_dimmed_alpha, DIMMED_ALPHA)
            recycle()
        }
    }

    //不用重写onMeasure，反正要铺满的

    //在onSizeChange中给path和paint赋值,其实也可以只在这里记录w，h然后在onDraw里给这些东西赋值
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w.toFloat()
        this.h = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPath()
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        canvas.drawPath(mDimmedPath, mPaint)
        mPaint.xfermode = xFerMode
        canvas.drawPath(mCenterRectPath, mPaint)
        mPaint.xfermode = null
        canvas.restoreToCount(layerId)
    }

    private fun initPath() {
        mDimmedPath.reset()
        mCenterRectPath.reset()
        //铺满全屏的阴影蒙板
        mDimmedPath.addRect(0f, 0f, w, h, Path.Direction.CW)
        /*开始计算中间抠图层*/
        //获得中心点坐标
        val centerX = w / 2
        val centerY = h / 2
        //中间抠图层和左右两边的边距 这里写死为view最小的边的 1/16
        val minSize = Math.min(w, h)
        val space = minSize / 20 * 2
        //中间抠图层的宽高
        val width = minSize - space
        val height = width * heightRatio / widthRatio
        mCenterWidth = width
        mCenterHeight = height
        //获得中间抠图层的坐标
        val startX = centerX - width / 2
        val startY = centerY - height / 2
        val endX = centerX + width / 2
        val endY = centerY + height / 2
        mCenterRect = RectF(startX, startY, endX, endY)
        mCenterRectPath.addRect(mCenterRect, Path.Direction.CW)
    }

    /**
     * 剪裁图片
     */
    fun cropImg(v: ImageView): Bitmap {
        val bg = v.drawable.constantState!!.newDrawable()
        val bitmap = getBitmapByDrawable(bg)
        val left = mCenterRect.left.toInt()
        val top = mCenterRect.top.toInt()
        val right = mCenterRect.right.toInt()
        val bottom = mCenterRect.bottom.toInt()
        val result = Bitmap.createBitmap(bitmap, left, top, mCenterWidth.toInt(), mCenterHeight.toInt())
        return result
    }

    private fun getBitmapByDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            w.toInt(), h.toInt(),
            if (drawable.opacity != PixelFormat.OPAQUE)
                Bitmap.Config.ARGB_8888
            else
                Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w.toInt(), h.toInt())
        drawable.draw(canvas)
        return bitmap
    }
}