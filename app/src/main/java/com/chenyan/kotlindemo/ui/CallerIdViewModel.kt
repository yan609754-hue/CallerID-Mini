package com.chenyan.kotlindemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chenyan.kotlindemo.data.CallerIdRepository
import com.chenyan.kotlindemo.data.NumberInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CallerIdViewModel : ViewModel() {
    private val repository = CallerIdRepository()

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state = _state.asStateFlow();//只读

    fun query(number: String) {
        if (number.length < 3) {
            _state.value = UiState.Error("号码太短")
            return
        }
        //开启协程
        viewModelScope.launch {
            _state.value = UiState.Querying
            repository.identify(number)
                .catch { e -> _state.value = UiState.Error(e.message ?: "查询失败") }
                .collect { info ->
                    _state.value = when (info.source) {
                        NumberInfo.Source.LOCAL -> UiState.LocalHit(info)
                        NumberInfo.Source.CLOUD -> UiState.Complete(info)
                    }
                }
        }
    }

}