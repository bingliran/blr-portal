package com.blr19c.portal.base.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 抽象日志对象
 */
abstract class ProtectedLogger {

    /**
     * 日志对象
     */
    protected val log = LoggerFactory.getLogger(this::class.java)!!

    companion object {

        @JvmStatic
        fun <T> getLogger(clazz: Class<T>): Logger {
            return LoggerFactory.getLogger(clazz)!!
        }
    }
}