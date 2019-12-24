package com.stone.templateapp

import com.stone.templateapp.demo.proxy.*
import com.stone.templateapp.util.CompressUtils
import com.stone.templateapp.util.EDcryptUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.LineNumberReader

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun testMD5() {
        //959bf3c41b5b5aed7fb76eaeac7d8a3b
        assertEquals("959bf3c41b5b5aed7fb76eaeac7d8a3b", EDcryptUtils.toMD5("日了狗"))
    }


    @Test
    fun testFileMD5() {
//        CRC-32	71430673
//        MD5 Hash	49d5c8a4bd81219a3554ca1db9013cf8
//        SHA1 Hash	d899d9343d870f454317842f886aade1cb9d4238
//        SHA256 Hash	3541aa23675312943d270991792446b3a58b2c95d30002706e5e8d9107429347
        assertEquals("49d5c8a4bd81219a3554ca1db9013cf8", EDcryptUtils.toMD5(File("/Users/mac/Downloads/bundlejs.zip")))
    }

    @Test
    fun testUnZip() {
        CompressUtils.unzip("/Users/mac/Downloads/zip/归档.zip")
//        CompressUtils.unzip("/Users/mac/Downloads/zip/归档.zip", "/Users/mac/Downloads/zip")
    }

    @Test
    fun testZip() {
        CompressUtils.zip("/Users/mac/Downloads/zip/")
    }

    @Test
    fun testShell() {
        val result = StringBuilder()
        try {
//            val exec = Runtime.getRuntime().exec("./dumpsys battery") //no
//            val exec = Runtime.getRuntime().exec("/system/bin/dumpsys battery")//no
//            val exec = Runtime.getRuntime().exec("echo 我草你。。") // yes
            val exec = Runtime.getRuntime().exec("adb shell dumpsys battery") // no
            LineNumberReader(InputStreamReader(exec.inputStream)).use {
                var read = it.readLine()
                while (read != null) {
                    result.append(read.trim())
                    println(read.trim())
                    read = it.readLine()
                }
            }
//            Logs.i(MainActivity.TAG, result)
        } catch (e: IOException) {
            e.printStackTrace()
            result.append("the IOException occurred")
        }
        println(result)
    }

    @Test
    fun testKotlinObject() {
        println(TestObject.property + "  1111")
        assertEquals("1234", TestObject.property)
    }

    @Test
    fun testIsAssignableFrom() {
        println("String 是 Object 的父类 ${String::class.java.isAssignableFrom(Object::class.java)}")
        assertEquals(false, String::class.java.isAssignableFrom(Object::class.java))
        println("Object 是 String 的父类 ${Object::class.java.isAssignableFrom(String::class.java)}")
        assertEquals(true, Object::class.java.isAssignableFrom(String::class.java))
    }


    @Test
    fun testEncryptNumber() {
        println(encryptNumber("18923459876"))

        println(encryptNumber("18655519876"))
    }

    private fun encryptNumber(number: String): String {
        return number.replaceRange(3, 7, "****")
    }

    @Test
    fun testDynamicProxy() {
        val target = RealSubject()
        println("====================")
        val proxy = DynamicProxy().newProxy<Subject>(target)
//        proxy.execute()
        proxy.execute("test")
        println("===${proxy.javaClass}")

        println("====================")

        val newProxy = DynamicProxyJava().newProxy<Subject>(target)
//        newProxy.execute()
        newProxy.execute("test Java")

        println("===${newProxy.javaClass}")
    }

    @Test
    fun testStaticProxy() {
        val target = RealSubject()
        val proxy = StaticProxy(target)
        proxy.execute()
        proxy.execute("test")
    }

    @Test
    fun testCglibProxy() {
        val target = CglibSubject()
        val factory = CglibProxyFactory(target)
        println("target:${target.javaClass}")
        val proxy = factory.getProxyInstance<CglibSubject>()
        println("proxy:${proxy.javaClass}")
        proxy.print()
        println("=================")
        proxy.input("【test cglib proxy】")
    }

    @Test
    fun testCglibProxyJava() {
        val target = CglibSubjectJava()
        val factory = CglibProxyJavaFactory(target)
        println("target:${target.javaClass}")
        val proxy = factory.getProxyInstance<CglibSubjectJava>()
        println("proxy:${proxy.javaClass}")
        proxy.print()
        println("=================")
        proxy.input("【test cglib proxy】")
    }

}
