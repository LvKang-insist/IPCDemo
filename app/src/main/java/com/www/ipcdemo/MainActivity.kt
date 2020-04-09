package com.www.ipcdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.www.ipcdemo.service.BinderPoll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binderPoll: BinderPoll
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //打开Service池
        binderPoll = BinderPoll.getInstance(this)

        main_tv.setOnClickListener {
            doWork()
        }
    }

    private fun doWork() {

        //从 binderPoll 获取指定的 Binder
        val securityBinder =
            binderPoll.queryBinder(BinderPoll.BINDER_SECURITY_CENTER)
        //将 Binder 转为对应的 AIDL 接口所属的类型
        val iSecurityCenter = ISecurityCenter.Stub.asInterface(securityBinder)

        //调用服务端的方法
        val encrypt = iSecurityCenter.encrypt("Hello Android")
        Log.e("encrypt ：", encrypt)
        Log.e("securityBinder ：", iSecurityCenter.decrypt(encrypt))

        //------------------------

        val computeBinder = binderPoll.queryBinder(BinderPoll.BINDER_COMPUTE)
        val iCompute = ICompute.Stub.asInterface(computeBinder)
        Log.e("6+6", iCompute.add(6, 6).toString())


        //如果有新的服务，只需要新建 AIDL 接口，创建一个实现类
        //在 BinderPoll 中 添加对应的标记，并且返回对应的 IBinder 即可
        //使用和上面的一样

    }
}
