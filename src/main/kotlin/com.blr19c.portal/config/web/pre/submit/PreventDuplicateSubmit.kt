package com.blr19c.portal.config.web.pre.submit

import org.aspectj.lang.ProceedingJoinPoint
import java.time.Duration
import kotlin.reflect.KClass

/**
 * 防止重复提交
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PreventDuplicateSubmit(

        /**
         * 防止重复的持续时间
         * @see Duration
         */
        val duration: String,

        /**
         * 使用某种方式作为重复验证
         */
        val repeatVerify: KClass<out RepeatVerifyFunction>) {
    /**
     * PreventDuplicateSubmit的重复校验实现
     */
    interface RepeatVerifyFunction {
        fun getKey(pjp: ProceedingJoinPoint?): String
    }
}