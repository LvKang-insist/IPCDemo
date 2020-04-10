package com.www.ipcdemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.appcompat.widget.LinearLayoutCompat

class MyLinearLayout : LinearLayoutCompat {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    )

    private var velocity: VelocityTracker = VelocityTracker.obtain()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                velocity.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                velocity.addMovement(event)
            }
            MotionEvent.ACTION_UP -> {
                velocity.computeCurrentVelocity(1000)
                Log.e("--------", velocity.xVelocity.toString())
                Log.e("--------", velocity.yVelocity.toString())
            }
        }
        return true
    }

}