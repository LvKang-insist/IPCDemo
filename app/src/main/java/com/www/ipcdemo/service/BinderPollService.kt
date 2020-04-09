package com.www.ipcdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BinderPollService : Service() {

    private val mBinderPoll = BinderPoll.Companion.BinderPoolImpl()

    override fun onBind(intent: Intent?): IBinder? {
        return mBinderPoll.asBinder()
    }

}
