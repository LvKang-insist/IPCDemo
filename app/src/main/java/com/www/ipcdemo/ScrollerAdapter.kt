package com.www.ipcdemo

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.www.ipcdemo.page.PageAdapter

class ScrollerAdapter(layoutRes: Int) : PageAdapter(layoutRes) {
    val array = arrayOf(Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN)

    override fun count(): Int {
        return 4
    }

    val url = arrayOf(
        "https://wanandroid.com/blogimgs/7a8c08d1-35cb-43cd-a302-ce9b0f89fc59.png",
        "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png",
        "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
        "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png"
    )

    override fun view(view: View, position: Int) {
        Log.e("哈哈哈哈：", "$position")
        view.setOnClickListener {
            Toast.makeText(view.context, "$position", Toast.LENGTH_LONG).show()
        }
        Glide.with(view.context)
            .load(url[position])
            .into((view as AppCompatImageView))
    }

}