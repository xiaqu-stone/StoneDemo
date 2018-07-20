package com.stone.templateapp.demo.binderpool

/**
 * Created By: sqq
 * Created Time: 18/7/19 下午7:24.
 */
class ComputeImpl : ICompute.Stub() {
    //计算模块的Binder类
    override fun add(a: Int, b: Int): Int {
        println("ComputeImpl invoke add")
        return a + b
    }
}