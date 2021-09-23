package com.blr19c.portal.config.web.exception

import com.blr19c.portal.base.exception.BaseException
import com.blr19c.portal.base.log.ProtectedLogger
import com.blr19c.portal.base.network.ResponseData
import com.blr19c.portal.base.network.ResponseData.Companion.fail
import com.blr19c.portal.base.network.ResponseData.Companion.just
import com.blr19c.portal.base.network.ResponseEnum
import com.blr19c.portal.modules.user.foreground.exception.IncorrectUsernameOrPasswordException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.io.FileNotFoundException

/**
 * 全局异常处理
 */
@RestControllerAdvice
class GlobalExceptionHandler : ProtectedLogger() {

    /**
     * 全局可提示异常
     */
    @ExceptionHandler(BaseException::class)
    fun baseException(e: BaseException): ResponseData<*> {
        if (log.isDebugEnabled)
            log.debug(e.message)
        return fail(e.message!!)
    }

    /**
     * 访问路径不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException::class, FileNotFoundException::class)
    fun noHandlerFoundException(e: Exception): ResponseData<*> {
        if (log.isDebugEnabled)
            log.debug(e.message)
        return just<Any>(ResponseEnum.NO_HANDLER_FOUND)
    }

    /**
     * 账号密码错误
     */
    @ExceptionHandler(IncorrectUsernameOrPasswordException::class)
    fun incorrectUsernameOrPasswordException(e: Exception): ResponseData<*> {
        if (log.isDebugEnabled)
            log.debug(e.message)
        return just<Any>(ResponseEnum.INCORRECT_ACCOUNT_PASSWORD)
    }
}