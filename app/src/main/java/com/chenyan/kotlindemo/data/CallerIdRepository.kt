package com.chenyan.kotlindemo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * 双通道仓库
 */
class CallerIdRepository(
    private val localDataSource: LocalDataSource = LocalDataSource(),
    private val cloudDataSource: CloudDataSource = CloudDataSource()
) {

    fun identify(number:String):Flow<NumberInfo> = channelFlow {
        // 通道一：本地，毫秒级先出
        launch {
            localDataSource.query(number).collect { send(it) }
        }
        // 通道二：云端，异步后出；2 秒超时即放弃本次，UI 永不等待
        launch {
            val cloudResult = withTimeoutOrNull(2000) { cloudDataSource.query(number) }
            // 假如超时了，query方法会被取消掉，cloudResult为null，是为兜底逻辑
            // 超时也发一条：tag 为 null 的云端结果 → UI 走 Complete 分支显示"云端无此号码标记"
            val result = cloudResult ?: NumberInfo(
                number = number, location = null,
                tag = null, source = NumberInfo.Source.CLOUD
            )
            //存入缓存
            localDataSource.cacheTag(number, result.tag)
            send(result)
        }
    }
}