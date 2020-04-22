package com.www.ipcdemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class ItemListLayout : ViewGroup {

    /**
     * 每行的数量
     */
    var horCount = 5

    /**
     * 行高
     */
    var horHeight = dp2px(40)

    /**
     * 水平间距
     */
    var horInterval = dp2px(15)

    /**
     * 垂直间距
     */
    var verInterval = dp2px(10)


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context, attributeSet, def
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //测量子布局
        for (i in 0 until childCount) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val lineCount = childCount / horCount
        var childWidth = width / horCount
        var height = 0
        val top = 0

        for (i in 0 until lineCount) {
            for (j in 0 until horCount) {
                val view = getChildAt((i * horCount) + j)
                val ll =
                    if (j == 0) j * view.measuredWidth else j * view.measuredWidth + verInterval
                val tt = top * view.measuredHeight
                val rr = (j + 1) * view.measuredWidth
                val bb = (top + 1) * measuredHeight
                view.layout(ll, tt, rr, bb)
            }
        }
    }


    /**
     * 将 dp 转为 px
     */
    fun dp2px(dpValue: Int): Int {
        val metrics = resources.displayMetrics
        return (metrics.density * dpValue + 0.5f).toInt()
    }
}