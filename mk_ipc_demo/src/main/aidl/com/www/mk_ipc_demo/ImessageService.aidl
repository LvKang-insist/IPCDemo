package com.www.mk_ipc_demo;

import com.www.mk_ipc_demo.entity.Message;
import com.www.mk_ipc_demo.MessageReceiverListener;


/**
 * 消息服务
 */
interface ImessageService {

    void sendMessage(out Message message);

    void registerMessageReceiveListener(MessageReceiverListener listener);

    void unRegisterMessageRececleListener(MessageReceiverListener listener);

}
