package com.www.ipcdemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.Toast

class ItemListLayout : ViewGroup {

    /**
     * 每行的数量，
     */
    var horCount = 5

    /**
     * 行高
     */
    var lineHeight = dp2px(40)

    /**
     * 水平间距
     */
    var horInterval = dp2px(10)

    /**
     * 垂直间距
     */
    var verInterval = dp2px(10)

    /**
     * View 集
     */
    private var views: MutableList<View> = mutableListOf()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var mAdapter: ItemListAdapter? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context, attributeSet, def
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        // 如果在布局文件中设置的宽高都是固定值[比如100dp、200dp等]，就用下边方式直接获取宽高
        var width = MeasureSpec.getSize(widthMeasureSpec);
        var height = MeasureSpec.getSize(heightMeasureSpec);

        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
        val heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = widthMeasureSpec + paddingLeft + paddingRight
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            val heightCount = childCount / horCount
            height =
                (heightCount * lineHeight) + (heightCount - 1) * verInterval + paddingTop + paddingBottom
        }

        setMeasuredDimension(width, height)

        //测量子布局
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Toast.makeText(context, "哈哈·", Toast.LENGTH_LONG).show()
        //行
        val lineCount = childCount / horCount
        //宽
        val childWidth =
            (width - ((horCount - 1) * horInterval) - (paddingLeft + paddingRight)) / horCount

        for (i in 0 until lineCount) {
            for (j in 0 until horCount) {
                val view = getChildAt((i * horCount) + j)
                val ll =
                    if (j == 0) j * childWidth else (j * childWidth) + (horInterval * j)
                val tt = if (i == 0) i * lineHeight else (i * lineHeight) + (verInterval * i)
                val rr =
                    if (j == 0) (j + 1) * childWidth else ((j + 1) * childWidth) + (horInterval * j)
                val bb =
                    if (i == 0) (i + 1) * lineHeight else ((i + 1) * lineHeight) + (verInterval * i)
                view.layout(
                    ll + paddingLeft, tt + paddingTop, (rr + paddingLeft), bb + paddingTop
                )
            }
        }
    }

    fun setAdapter(adapter: ItemListAdapter) {
        this.mAdapter = adapter
        initData()
    }

    private fun initData() {
        if (mAdapter != null) {
            val count = mAdapter!!.getCount()
            val resId = mAdapter!!.gerResId()
            for (i in 0 until count) {
                val view = inflater.inflate(resId, null, false)
                setGestureDetector(view)
                addView(view)
                views.add(view)
            }
            for (i in 0 until count) {
                mAdapter?.view(views[i], i)
            }
        }
    }

    /**
     * 处理事件
     */
    private fun setGestureDetector(view: View) {
        var downX = 0f
        var downY = 0f
        var isMove = false
        var vX = 0f
        var vY = 0f
        val gestureDetector = GestureDetector(GestureListener(view))
        //触摸事件
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    downY = event.rawY
                    vX = view.x
                    vY = view.y
                }
                MotionEvent.ACTION_MOVE -> {
                    isMove = true
                    view.animate().translationX(event.rawX - downX).duration = 0
                    view.animate().translationY(event.rawY - downY).duration = 0
                }
                MotionEvent.ACTION_UP -> {
                    val fx = (view.x - vX)
                    val fy = view.y - vY
                }
            }
            true
        }
        //获取焦点
        view.isFocusable = true;
        //启用或者禁用 单击事件
        view.isClickable = true;
        //启用或者禁用 长按事件
        view.isLongClickable = true;
    }

    class OnTouchEvent(val gestureDetector: GestureDetector) : OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }
    }

    /**
     * 将 dp 转为 px
     */
    private fun dp2px(dpValue: Int): Int {
        val metrics = resources.displayMetrics
        return (metrics.density * dpValue + 0.5f).toInt()
    }
}