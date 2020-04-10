package com.www.ipcdemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.ViewConfigurationCompat
import kotlin.math.abs

class ScrollerLayout : ViewGroup {

    /**
     * 用于完成滚动操作的实例
     */
    private var mScroller: Scroller

    /**
     * 判定为拖动的最小移动像素
     */
    private var mTouchSlop = 0

    /**
     * 按下时的屏幕坐标
     */
    private var mXDown = 0f

    /**
     * 移动时所处的屏幕坐标
     */
    private var mXMove = 0f

    /**
     * 上次触发 MOVE 事件的屏幕坐标
     */
    private var mXLastMove = 0f

    /**
     * 界面可滚动的左边界
     */
    private var leftBorder = 0

    /**
     * 界面可滚动的由边界
     */
    private var rightBorder = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        mScroller = Scroller(context)
        //最小移动距离
        mTouchSlop = ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(context))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //测量子控件的大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            val childCount = childCount
            for (i in 0 until childCount) {
                val childView = getChildAt(i)
                //给每一个子控件在水平方向上布局
                childView.layout(
                    i * childView.measuredWidth, 0,
                    (i + 1) * childView.measuredWidth, childView.measuredHeight
                )
            }
            //获取左右边界
            leftBorder = getChildAt(0).left
            rightBorder = getChildAt(childCount - 1).right
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //rawX ，相对于屏幕的横坐标
                mXDown = ev.rawX
                mXLastMove = mXDown
            }
            MotionEvent.ACTION_MOVE -> {
                mXMove = ev.rawX
                //移动的距离
                val diff = abs(mXMove - mXDown)
                mXLastMove = mXMove
                //当手指拖动大于 TouchSlop 值时，认为应该进行滚动，拦截子控件的时间
                if (diff > mTouchSlop) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {

                mXMove = event.rawX
                //移动的距离
                val scrolledX = (mXLastMove - mXMove).toInt()
                //边界处理，拖出边界后就使用 scrollTo 回到边界位置
                if (scrollX + scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0)
                    return true
                } else if (scrollX + width + scrolledX > rightBorder) {
                    scrollTo(rightBorder - width, 0)
                    return true
                }
                //移动
                scrollBy(scrolledX, 0)
                mXLastMove = mXMove
            }
            MotionEvent.ACTION_UP -> {
                //当手指抬起时，根据当前滚动值来判定应该滚动到那个子控件界面
                val targetIndex = (scrollX + width / 2) / width
                val dx = targetIndex * width - scrollX
                Log.e("onTouchEvent","$targetIndex  ----- $dx")
                //第二步，调用 startScroll 方法来初始化数据并刷新界面
                mScroller.startScroll(scrollX, 0, dx, 0)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        //第三步，重新 computeScroll 方法，并在内部完成平滑滚动逻辑
        //判断滚动操作是否完成了，如果没有完成就继续滚动
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}