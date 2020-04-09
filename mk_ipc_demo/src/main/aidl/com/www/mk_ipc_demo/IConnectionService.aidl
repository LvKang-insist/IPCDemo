// IConnectionService.aidl
package com.www.mk_ipc_demo;

/**
 * 连接服务
 */
interface IConnectionService {

   oneway void connect();

    void disconnect();

    boolean isConnected();
}
