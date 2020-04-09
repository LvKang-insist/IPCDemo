package com.www.mk_ipc_demo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.www.mk_ipc_demo.entity.Message
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //启动子进程的 Service
        initAIDLService()

        initMessengerService()
    }

    private var messengerProxy: Messenger? = null

    //服务端传递过来的消息
    private val clientMessenger = Messenger(object : Handler(Looper.myLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            val data = msg.data
            val string = data.getString("clientMessage")
            Toast.makeText(
                this@MainActivity,
                "${android.os.Process.myPid()} -- $string", Toast.LENGTH_LONG
            ).show()
        }
    })

    private fun initMessengerService() {
        messenger.setOnClickListener {
            val message = android.os.Message()
            val bundle = Bundle()
            bundle.putString("message", "我是主线程通过 Messenger 发送的消息")
            message.data = bundle
            //把客户端用于接收子进程数据 clientMessenger 传递过去，
            //子进程通过 clientMessenger 给客户端传递消息
            message.replyTo = clientMessenger
            //给子进程发送消息
            messengerProxy?.send(message)
        }

        bindService(Intent(this, MessengerService::class.java), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                messengerProxy = Messenger(service)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }, Context.BIND_AUTO_CREATE)
    }


    //连接服务
    private var connectionServiceProxy: IConnectionService? = null
    //消息服务
    private var messageServiceProxy: ImessageService? = null
    //管理Service
    private var serviceManagerProxy: IServiceManager? = null

    //注册消息事件监听
    //这个监听也是一个 AIDL 的实现，和在 RemoteService 中的实现是一样的，只不过是一个反向的
    //消息服务注册该事件后，就可在子进程中进行调用了
    private val messageReceiveListener = object : MessageReceiverListener.Stub() {
        override fun onReceiveMessage(message: Message?) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity,
                    "${android.os.Process.myPid()} -- ${message?.content}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initAIDLService() {

        //三个 button，用于连接服务
        connect.setOnClickListener {
            //这里也会阻塞50000
            connectionServiceProxy?.connect()
        }
        dis_connect.setOnClickListener {
            connectionServiceProxy?.disconnect()
        }
        is_connect.setOnClickListener {
            val isconnected = connectionServiceProxy?.isConnected
            Toast.makeText(this, "$isconnected", Toast.LENGTH_LONG).show()
        }

        //三个 button，用户消息服务
        send_message.setOnClickListener {
            val message = Message()
            message.content = "主进程发送的消息"
            messageServiceProxy?.sendMessage(message)
            Log.e("-----", "主进程 发送完消息----------${message.isSendSuccess}")
        }
        register_listener.setOnClickListener {
            //添加消息监听
            messageServiceProxy?.registerMessageReceiveListener(messageReceiveListener)
        }
        unregister_listener.setOnClickListener {
            //移除消息监听
            messageServiceProxy?.unRegisterMessageRececleListener(messageReceiveListener)
        }

        bindService(Intent(this, RemoteService::class.java), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                serviceManagerProxy = IServiceManager.Stub.asInterface(service)
                //获取连接服务
                connectionServiceProxy = IConnectionService.Stub.asInterface(
                    serviceManagerProxy?.getService(IConnectionService::class.java.simpleName)
                )
                //获取消息服务
                messageServiceProxy = ImessageService.Stub.asInterface(
                    serviceManagerProxy?.getService(ImessageService::class.java.simpleName)
                )
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                //销毁时调用,可用于重新连接
                Toast.makeText(this@MainActivity, "$name 销毁了", Toast.LENGTH_LONG).show()
            }

        }, Context.BIND_AUTO_CREATE)
    }

}
