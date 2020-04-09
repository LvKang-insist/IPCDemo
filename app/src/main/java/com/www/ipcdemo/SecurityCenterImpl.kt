package com.www.ipcdemo

class SecurityCenterImpl : ISecurityCenter.Stub() {

    companion object {
        const val SECRET_CODE = '^'
    }

    /**
     * 加密
     */
    override fun encrypt(content: String): String {
        val chars = content.toCharArray()
        chars[0] = '1'
        for (i in chars.indices) {
            chars[i] = chars[i].toInt().xor(SECRET_CODE.toInt()).toChar()
        }
        return String(chars)
    }

    /**
     * 解密
     */
    override fun decrypt(password: String): String {
        return encrypt(password)
    }

}