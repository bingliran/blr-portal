package com.blr19c.portal.config.web.pre.submit

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.annotation.Order
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.createInstance

/**
 * 防止重复提交切面
 */
@Aspect
@Component
@Order(0)
class PreventDuplicateSubmitAspect(private val redisTemplate: StringRedisTemplate) {

    @Around("@annotation(com.blr19c.portal.config.web.pre.submit.PreventDuplicateSubmit)")
    fun around(pjp: ProceedingJoinPoint): Any {
        if (pjp.signature !is MethodSignature)
            return pjp.proceed()
        val preventDuplicateSubmit = AnnotationUtils.getAnnotation((pjp.signature as MethodSignature).method,
                PreventDuplicateSubmit::class.java) ?: return pjp.proceed()
        val duration = Duration.parse(preventDuplicateSubmit.duration)
        val repeatVerify = preventDuplicateSubmit.repeatVerify.createInstance()
        val key = repeatVerify.getKey(pjp)
        val expire: Long = redisTemplate.getExpire(key)
        if (expire > 0) {
            throw PreventDuplicateSubmitException(expire.toInt())
        }
        redisTemplate.opsForValue().set(key, duration.toString(), duration.seconds, TimeUnit.SECONDS)
        return pjp.proceed()
    }
}