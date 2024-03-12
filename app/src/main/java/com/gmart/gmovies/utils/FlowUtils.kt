package com.gmart.gmovies.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
fun <T1, T2, T3, T4, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    transform: suspend (T1, T2, T3, T4) -> R,
): Flow<R> = combine(flow, flow2, flow3, flow4) { args: Array<*> ->
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
    )
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) : Serializable