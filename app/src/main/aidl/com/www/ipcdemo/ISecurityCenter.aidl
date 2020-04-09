// ISecurityCenter.aidl
package com.www.ipcdemo;

/**
 * 提供加密解密功能
 */
interface ISecurityCenter {
    String encrypt(String content);

    String decrypt(String password);
}
