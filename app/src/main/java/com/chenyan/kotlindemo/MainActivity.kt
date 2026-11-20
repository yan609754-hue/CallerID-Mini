package com.chenyan.kotlindemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chenyan.kotlindemo.databinding.ActivityMainBinding
import com.chenyan.kotlindemo.ui.CallerIdViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chenyan.kotlindemo.ui.UiState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: CallerIdViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnQuery.setOnClickListener{
            viewModel.query(binding.etNumber.text.toString().trim())
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
               viewModel.state.collect{state->render(state)}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun render(state: UiState) = with(binding) {
        when (state) {
            is UiState.Idle -> {
                tvLocation.text = ""; tvTag.text = ""
            }

            is UiState.Querying -> {
                tvLocation.text = "查询中…"; tvTag.text = ""
            }

            is UiState.LocalHit -> {
                tvLocation.text = "归属地：" + (state.info.location ?: "未知") + "（本地）"
                tvTag.text =
                    if (state.info.tag != null) "标记：" + state.info.tag +"（缓存命中）" else "云端识别中…"
            }

            is UiState.Complete -> {
                tvTag.text =
                    if (state.info.tag != null) "标记：" + state.info.tag else "云端无此号码标记"
            }

            is UiState.Error -> tvLocation.text = "出现错误：" + state.message
        }

    }
}