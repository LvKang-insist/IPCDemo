package com.www.ipcdemo.view

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class GestureListener(val view: View) : GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private var moveX = 0f
    private var moveY = 0f

    override fun onDown(e: MotionEvent): Boolean {

        return false
    }

    //用户按下屏幕，尚未松开或 者拖动 则会触发
    override fun onShowPress(e: MotionEvent) {
        moveX = e.rawX
        moveY = e.rawY
        Log.e(" onShowPress ：", "${e.x} --- ${e.y}")
    }


    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {

        when (e2.action) {
            MotionEvent.ACTION_MOVE -> {
                view.animate().translationX(moveX + distanceX).duration = 0
                view.animate().translationY(moveY +distanceY).duration = 0
            }
            MotionEvent.ACTION_UP -> {

            }
        }


        Log.e(" onScroll e1：", "${e1.x} --- ${e1.y}")
        Log.e(" onScroll e2：", "${e2.x} --- ${e2.y}")
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }


    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        return false;
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false;
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false;
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Toast.makeText(view.context, "抬起", Toast.LENGTH_LONG).show()
        return false;
    }


}