package com.www.ipcdemo.view

import android.view.View

abstract class ItemListAdapter {


    abstract fun getCount(): Int

    abstract fun gerResId(): Int

    abstract fun view(view: View, position: Int)

}