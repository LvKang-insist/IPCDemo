package com.www.mk_ipc_demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Toast
import com.www.mk_ipc_demo.entity.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 管理和提供子进程的连接和消息服务
 */
class RemoteService : Service() {

    /**
     * 连接状态
     */
    private var isConnected: Boolean = false

    //注册消息监听的集合
    private val messageRemoteListener = RemoteCallbackList<MessageReceiverListener>()

    private var scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor? = null

    private var scheduledFuture: ScheduledFuture<*>? = null


    /**
     * Stub 是实现类中的抽象类
     * 这三个方法就是 aidl 文件中的三个方法，在这里进行实现
     */
    val connectionService =
        object : IConnectionService.Stub() {
            //当主进程调用 connect 时，这里个 connect 就会执行
            override fun connect() {
                //模拟连接耗时
                Thread.sleep(5000)
                this@RemoteService.isConnected = true
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@RemoteService, "connect", Toast.LENGTH_LONG).show()
                }
                //每隔 5 秒钟，如有监听，则发送消息
                scheduledFuture =
                    scheduledThreadPoolExecutor?.scheduleAtFixedRate(object : Runnable {
                        override fun run() {
                            val size = messageRemoteListener.beginBroadcast()
                            for (i in 0 until size) {
                                val message = Message()
                                message.content = "我是子进程发送的消息"
                                messageRemoteListener.getBroadcastItem(i).onReceiveMessage(message)
                            }
                            messageRemoteListener.finishBroadcast()
                        }
                    }, 5000, 5000, TimeUnit.MILLISECONDS)
            }

            //当主进程调用 disconnect 时，这里个 disconnect 就会执行
            override fun disconnect() {
                this@RemoteService.isConnected = false
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(this@RemoteService, "disconnect", Toast.LENGTH_LONG).show()
                }
                scheduledFuture?.cancel(true)
            }

            override fun isConnected(): Boolean {
                return this@RemoteService.isConnected
            }

        }

    /**
     * 消息服务的实现
     */
    val messageService = object : ImessageService.Stub() {
        override fun sendMessage(message: Message?) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    this@RemoteService,
                    "${android.os.Process.myPid()} -- ${message?.content}",
                    Toast.LENGTH_LONG
                ).show()
            }
            //设置消息状态
            message?.isSendSuccess = isConnected
            Log.e("-----", "子进程 接收消息----------${message?.isSendSuccess}")
        }

        override fun registerMessageReceiveListener(listener: MessageReceiverListener?) {
            if (listener != null) messageRemoteListener.register(listener)
        }

        override fun unRegisterMessageRececleListener(listener: MessageReceiverListener?) {
            if (listener != null) messageRemoteListener.unregister(listener)
        }

    }

    /**
     * Service 管理，可让主进程选择性的调用连接服务或者消息服务
     */
    private val serviceManager = object : IServiceManager.Stub() {
        override fun getService(serviceName: String?): IBinder? {
            if (IConnectionService::class.java.simpleName == serviceName) {
                return connectionService.asBinder()
            } else if (ImessageService::class.java.simpleName == serviceName) {
                return messageService.asBinder()
            } else
                return null
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return serviceManager.asBinder()
    }

    override fun onCreate() {
        super.onCreate()
        scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(1)
    }


}