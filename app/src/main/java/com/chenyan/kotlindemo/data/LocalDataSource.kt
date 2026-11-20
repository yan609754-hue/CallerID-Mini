package com.chenyan.kotlindemo.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 本地通道
 */
class LocalDataSource {
    // 模拟内置归属地号段
    private val prefixMap= mapOf("138" to "广东深圳", "139" to "广东广州", "135" to "北京",
        "186" to "上海", "170" to "虚拟运营商", "400" to "企业客服")

    fun query(number: String): Flow<NumberInfo> = flow {
        delay(300) //模拟查询
        val location = prefixMap[number.take(3)] ?: "未知归属地"
        emit(NumberInfo(number = number, location = location, source = NumberInfo.Source.LOCAL))
    }

}