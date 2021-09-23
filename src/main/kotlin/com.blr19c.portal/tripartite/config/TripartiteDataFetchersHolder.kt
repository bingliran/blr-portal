package com.blr19c.portal.tripartite.config

import com.blr19c.portal.tripartite.common.AbstractDataFetchers
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 保存@TripartiteDataFetchers
 */
object TripartiteDataFetchersHolder {
    private val TRIPARTITE_DATA_FETCHERS_MAP: MutableMap<String, AbstractDataFetchers> = ConcurrentHashMap()

    /**
     * 添加提取器实例
     */
    fun addTripartiteDataFetchers(name: String, abstractDataFetchers: AbstractDataFetchers) {
        TRIPARTITE_DATA_FETCHERS_MAP.merge(name, abstractDataFetchers)
        { _, _ -> throw IllegalArgumentException(name + "提取器已存在") }
    }

    /**
     * 获取提取器实例列表
     */
    fun getTripartiteDataFetchersList(): List<AbstractDataFetchers> {
        return Collections.unmodifiableList(ArrayList(TRIPARTITE_DATA_FETCHERS_MAP.values))
    }

    /**
     * 获取提取器实例名称列表
     */
    fun getTripartiteDataFetchersNameList(): List<String> {
        return Collections.unmodifiableList(ArrayList(TRIPARTITE_DATA_FETCHERS_MAP.keys))
    }

    /**
     * 获取提取器实例列表
     */
    fun getTripartiteDataFetchers(name: String): AbstractDataFetchers? {
        return TRIPARTITE_DATA_FETCHERS_MAP[name]
    }
}