// IServiceManager.aidl
package com.www.mk_ipc_demo;


interface IServiceManager {
    IBinder getService(String serviceName);
}
