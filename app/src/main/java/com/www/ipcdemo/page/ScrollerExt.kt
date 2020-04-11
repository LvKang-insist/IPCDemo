package com.www.ipcdemo.page

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class ScrollerExt : Scroller {

    /**
     * 滚动操作是否完成，true为未完成，false 即完成
     */
    interface ScrollListener {
        fun isScroll(isScroll: Boolean)
    }

    var listener: ScrollListener? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)
    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(
        context,
        interpolator,
        flywheel
    )


    override fun computeScrollOffset(): Boolean {
        val offset = super.computeScrollOffset()
        if (listener != null) {
            listener?.isScroll(offset)
        }
        return offset
    }
}
