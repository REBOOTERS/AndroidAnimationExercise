package com.engineer.ai.util

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 协程线程切换框架
 * 示例用法：
 * AsyncExecutor(Dispatchers.IO)          // 初始线程
 *     .execute { fetchDataFromApi() }   // 执行耗时操作
 *     .switchTo(Dispatchers.Main)       // 切换线程
 *     .execute { updateUI() }           // 更新UI
 *     .onError { handleError(it) }      // 错误处理
 */
class AsyncExecutor(
    private val context: CoroutineContext = Dispatchers.Default, private val parentJob: Job? = null
) {
    private val scope: CoroutineScope by lazy {
        CoroutineScope(context + Job(parentJob ?: SupervisorJob()))
    }
    internal var deferred: Deferred<Any?>? = null
    private var errorHandler: (Throwable) -> Unit = {}

    // 核心执行方法
    fun execute(block: suspend () -> Unit): AsyncExecutor {
        deferred = scope.async {
            try {
                block()
            } catch (e: Throwable) {
                errorHandler(e)
                throw e
            }
        }
        return this
    }

    // 线程切换方法
    fun switchTo(newContext: CoroutineContext): AsyncExecutor {
        return AsyncExecutor(newContext, deferred?.let { Job(it) })
    }

    // 错误处理
    fun onError(handler: (Throwable) -> Unit): AsyncExecutor {
        errorHandler = handler
        return this
    }

    // 取消任务
    fun cancel() {
        scope.cancel()
    }

    companion object {
        // 快捷入口
        fun fromIO() = AsyncExecutor(Dispatchers.IO)
        fun fromMain() = AsyncExecutor(Dispatchers.Main)
        fun fromDefault() = AsyncExecutor(Dispatchers.Default)
    }
}

// 扩展函数：自动处理结果到主线程
suspend fun <T> AsyncExecutor.awaitResult(
    onSuccess: (T) -> Unit, onError: (Throwable) -> Unit = {}
): AsyncExecutor {
    return this.apply {
        try {
            val result = deferred?.await() as T
            Log.d("StyleTransferProcessor", "result is is ok")
            switchTo(Dispatchers.Main).execute { onSuccess(result) }
        } catch (e: Throwable) {
            Log.d("StyleTransferProcessor","onError ${e.stackTraceToString()}")
            switchTo(Dispatchers.Main).execute { onError(e) }
        }
    }
}