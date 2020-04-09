package com.www.mk_ipc_demo.entity

import android.os.Parcel
import android.os.Parcelable

class Message() : Parcelable {
    /**
     * 消息内容
     */
    var content: String? = null

    /**
     * 消息的状态
     */
    var isSendSuccess = false

    constructor(parcel: Parcel) : this() {
        content = parcel.readString()
        isSendSuccess = parcel.readByte().toInt() != 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeByte(if (isSendSuccess) 1.toByte() else 0.toByte())
    }

    fun readFromParcel(parcel: Parcel) {
        content = parcel.readString()
        isSendSuccess = parcel.readByte().toInt() == 1
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }

    }
}