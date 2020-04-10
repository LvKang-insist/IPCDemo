package com.www.ipcdemo.poll_service

import com.www.ipcdemo.ICompute

class ComputeImpl : ICompute.Stub() {
    override fun add(a: Int, b: Int): Int {
        return a + b
    }

}