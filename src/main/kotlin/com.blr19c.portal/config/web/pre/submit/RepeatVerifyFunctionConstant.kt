package com.blr19c.portal.config.web.pre.submit

import com.blr19c.portal.config.security.SecurityUtils
import com.blr19c.portal.config.web.pre.submit.PreventDuplicateSubmit.RepeatVerifyFunction
import com.blr19c.common.io.IPUtils
import com.blr19c.common.io.WebUtils
import org.aspectj.lang.ProceedingJoinPoint

/**
 * 重复验证方式
 */
interface RepeatVerifyFunctionConstant {

    /**
     * 使用ip判断
     */
    open class UseIP : RepeatVerifyFunction {
        override fun getKey(pjp: ProceedingJoinPoint?): String {
            return IPUtils.getIp(WebUtils.request())
        }

    }

    /**
     * 使用用户判断
     */
    open class UseUser : RepeatVerifyFunction {
        override fun getKey(pjp: ProceedingJoinPoint?): String {
            return SecurityUtils.authentication()!!.credentials as String
        }
    }
}