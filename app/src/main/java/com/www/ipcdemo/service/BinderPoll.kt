package com.www.ipcdemo.service

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.www.ipcdemo.poll_service.ComputeImpl
import com.www.ipcdemo.IBinderPool
import com.www.ipcdemo.poll_service.SecurityCenterImpl
import java.util.concurrent.CountDownLatch

/**
 * ：将所有的 AIDL 放在同一个 Service 中进行管理
 */
class BinderPoll(private val context: Context) {

    private var mBinderPoll: IBinderPool? = null;
    private var mConnectBinderPollCountDownLatch: CountDownLatch? = null

    companion object {
        const val BINDER_NONE = -1
        const val BINDER_COMPUTE = 0
        const val BINDER_SECURITY_CENTER = 1

        @SuppressLint("StaticFieldLeak")
        private var sInstance: BinderPoll? = null

        fun getInstance(context: Context): BinderPoll {
            if (sInstance == null) {
                synchronized(BinderPoll::class.java) {
                    sInstance =
                        BinderPoll(context)
                }
            }
            return sInstance!!
        }

        class BinderPoolImpl : IBinderPool.Stub() {
            override fun queryBinder(binderCode: Int): IBinder? {
                return when (binderCode) {
                    BINDER_SECURITY_CENTER -> {
                        SecurityCenterImpl().asBinder()
                    }
                    BINDER_COMPUTE -> {
                        ComputeImpl().asBinder()
                    }
                    else -> null
                }
            }
        }
    }

    init {
        connectBinderPoolService()
    }


    private fun connectBinderPoolService() {
        mConnectBinderPollCountDownLatch = CountDownLatch(1)
        //启动服务池
        val service = Intent(context, BinderPollService::class.java)
        context.bindService(service, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mBinderPoll = IBinderPool.Stub.asInterface(service)
                mBinderPoll?.asBinder()?.linkToDeath(mBinderPollDeathRecipient, 0)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }, Context.BIND_AUTO_CREATE)
    }

    /**
     * 从已经启动的 BinderPoll 中返回对应的 IBinder
     */
    fun queryBinder(binderCode: Int): IBinder {
        var binder: IBinder? = null
        if (mBinderPoll != null) {
            binder = mBinderPoll!!.queryBinder(binderCode)
        }
        return binder!!
    }

    //服务连接意外的断开后会回调 binderDied 方法
    private val mBinderPollDeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            Log.w("BinderPoll：", "binder died.")
            //取消监听
            mBinderPoll?.asBinder()?.unlinkToDeath(this, 0)
            mBinderPoll = null
            //重新启动BinderPoll
            connectBinderPoolService()
        }
    }

}