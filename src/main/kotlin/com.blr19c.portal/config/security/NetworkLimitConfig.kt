package com.blr19c.portal.config.security

import com.blr19c.common.io.IPUtils
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.util.concurrent.RateLimiter
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

/**
 * 对普通用户下行流量限制
 */
object NetworkLimitConfig {

    private val ipFlow = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(object : CacheLoader<String, FlowWindow>() {
                override fun load(key: String): FlowWindow {
                    return FlowWindow()
                }
            })

    /**
     * 获取NetworkLimitFilter
     */
    fun networkLimitFilter(): NetworkLimitFilter {
        return NetworkLimitFilter
    }

    /**
     * 流量限制
     */
    object NetworkLimitFilter : AnonymousAuthenticationFilter(UUID.randomUUID().toString()) {

        override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
            response as HttpServletResponse
            request as HttpServletRequest
            if (SecurityUtils.isPrivileged()) {
                return super.doFilter(request, response, chain)
            }
            //如果不是特权用户
            super.doFilter(request, object : HttpServletResponseWrapper(response) {
                override fun getOutputStream(): ServletOutputStream {
                    return NetworkLimitOutputStream(super.getOutputStream(), IPUtils.getIp(request))
                }
            }, chain)
        }
    }

    /**
     * 限速流
     */
    class NetworkLimitOutputStream(private val outputStream: ServletOutputStream,
                                   ip: String) : ServletOutputStream() {

        private val flowWindow: FlowWindow = ipFlow.get(ip)

        init {
            flowWindow.tryCount()
        }

        override fun isReady(): Boolean {
            return outputStream.isReady
        }

        override fun setWriteListener(listener: WriteListener?) {
            outputStream.setWriteListener(listener)
        }

        override fun flush() {
            outputStream.flush()
        }

        override fun write(b: Int) {
            flowWindow.tryAcquire(4)
            outputStream.write(b)
        }

        override fun write(b: ByteArray) {
            flowWindow.tryAcquire(b.size)
            outputStream.write(b)
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            flowWindow.tryAcquire(len)
            outputStream.write(b, off, len)
        }

        override fun close() {
            outputStream.close()
        }

    }

    @Suppress("UnstableApiUsage")
    class FlowWindow {

        private val bytesRateLimiter = RateLimiter.create((1024 * 1024 * 2).toDouble())!!
        private val countRateLimiter = RateLimiter.create(10.toDouble())!!

        fun tryAcquire(bytes: Int) {
            bytesRateLimiter.tryAcquire(bytes.coerceAtLeast(1), 1, TimeUnit.SECONDS)
        }

        fun tryCount() {
            countRateLimiter.acquire()
        }
    }
}