package com.chenyan.kotlindemo.data

import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 云端数据
 */
class CloudDataSource {
    private val tags= listOf("快递","外卖","出租车","骚扰电话","诈骗",null)

    // 耗时操作，只能在协程里面执行
    suspend fun query(number : String):NumberInfo{
        delay(1200 + Random.nextLong(800))
        return NumberInfo(number=number, tag = tags.random(), source = NumberInfo.Source.CLOUD)
    }
}