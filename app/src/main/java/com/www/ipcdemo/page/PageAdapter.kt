package com.www.ipcdemo.page

import android.view.View

abstract class PageAdapter(val layoutRes: Int) {

    abstract fun count(): Int

    abstract fun view(view: View, position: Int)

}