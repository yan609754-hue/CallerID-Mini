package com.chenyan.kotlindemo.ui

import com.chenyan.kotlindemo.data.NumberInfo

sealed interface UiState {
    data object Idle : UiState  //初始化
    data object Querying : UiState //查询中
    data class LocalHit(val info: NumberInfo) : UiState //本地归属地数据
    data class Complete(val info: NumberInfo) : UiState //云端回填：刷新标记
    data class Error(val message: String) : UiState
}