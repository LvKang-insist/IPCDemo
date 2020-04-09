package com.www.mk_ipc_demo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast

class MessengerService : Service() {

    @SuppressLint("HandlerLeak")
    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val string = bundle.getString("message")
            Toast.makeText(
                this@MessengerService,
                "${android.os.Process.myPid()} -- $string", Toast.LENGTH_LONG
            ).show()

            //三秒后返回消息给客户端
            val clientMessenger = msg.replyTo
            postDelayed({
                val message = Message.obtain()
                val clientBundle = Bundle()
                clientBundle.putString("clientMessage", "我是子进程返回的消息")
                message.data = clientBundle
                clientMessenger.send(message)
            }, 3000)
        }
    }

    private val messenger = Messenger(handler)



    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }

}
