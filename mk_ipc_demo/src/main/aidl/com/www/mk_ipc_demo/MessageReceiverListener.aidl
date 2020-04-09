// MessageReceiverListener.aidl
package com.www.mk_ipc_demo;
import com.www.mk_ipc_demo.entity.Message;

/**
 * 消息事件
 */
interface MessageReceiverListener {
    void onReceiveMessage(in Message message);
}
