# CallerID Mini（陌电识别迷你版）

来电号码识别的 Kotlin 个人实践项目：**双通道查询架构**——本地号段表毫秒级返回归属地，云端异步查询（2s 超时即弃）回填号码标记，UI 永不等待。

业务模型来自笔者 4 年 vivo 系统电话（Dialer）陌电识别模块的开发经验，用 Kotlin + 协程重新实现，聚焦语言与异步层的学习实践。

## 双通道时序

```
输入号码 ──┬─> 本地通道（号段表，~50ms）──────> 归属地立即显示
           └─> 云端通道（1.2–2s，2s 超时取消并兜底）──> 标记异步刷新
              （超时不悬挂：兜底发送空结果，UI 走"云端无标记"分支，给用户确定性）
```

## 技术点

- **Kotlin**：空安全、data class、sealed interface（界面状态机）、属性委托（by viewModels）
- **协程 + Flow**：channelFlow 双通道并发发送、withTimeoutOrNull 超时取消与兜底、suspend 挂起模型（delay vs Thread.sleep）
- **架构**：MVVM——ViewModel + StateFlow（对外只读）+ repeatOnLifecycle 生命周期感知收集
- **视图**：ViewBinding（编译期生成，零运行时反射）
- **测试**：runTest 虚拟时间验证双通道时序（本地必然先出、云端在后）
- **AI 辅助开发**：部分代码由 AI 辅助生成，经本人逐行理解、真机验证与问题修复（超时兜底为手工改进）

## 工程结构

```
app/src/main/java/com/chenyan/kotlindemo/
├── data/
│   ├── NumberInfo.kt          数据模型（data class + 可空标记业务语义）
│   ├── LocalDataSource.kt     本地通道：号段表 + Flow
│   ├── CloudDataSource.kt     云端通道：suspend + 模拟网络延迟
│   └── CallerIdRepository.kt  双通道仓库：channelFlow + 超时兜底（核心）
└── ui/
    ├── UiState.kt             sealed interface 界面状态机
    ├── CallerIdViewModel.kt   ViewModel + StateFlow
    └── MainActivity.kt        ViewBinding + repeatOnLifecycle 收集
```

## 作者

陈焱 · Android 开发（系统应用方向，10 年）
