package com.stone.templateapp.demo.binderpool

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午7:23.
 */
class SecurityCenterImpl : ISecurityCenter.Stub() {
    //加解密模块的Binder类
    companion object {
        const val SECRET_CODE = '^'.toInt()
    }

    override fun encrypt(content: String): String {
        println("SecurityCenterImpl invoke encrypt $content")
        val chars = content.toCharArray()
        for ((i, char) in chars.withIndex()) {
            //按位异或
            chars[i] = (char.toInt() xor SECRET_CODE).toChar()
        }
        return String(chars)
    }

    override fun decrypt(password: String): String {
        return encrypt(password)
    }
}