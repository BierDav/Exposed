package org.jetbrains.exposed.v1.core.utils

import co.touchlab.stately.collections.ConcurrentMutableMap
import org.jetbrains.exposed.v1.core.InternalApi

interface CacheWithSuspendableDefault<K, V> {
    suspend fun get(key: K): V
}

/** @suppress */
@InternalApi
class CachableMapWithSuspendableDefault<K, V>(
    private val map: MutableMap<K, V> = ConcurrentMutableMap(),
    val default: suspend (K) -> V
) : CacheWithSuspendableDefault<K, V> {
    override suspend fun get(key: K): V {
        return map.getOrPut(key) { default(key) }
    }
}
