package com.blr19c.portal.modules.user.foreground.exception

import com.blr19c.portal.base.exception.BaseException

/**
 * 用户名或密码错误异常
 */
open class IncorrectUsernameOrPasswordException
@JvmOverloads constructor(cause: Throwable? = null) : BaseException("用户名或密码错误", cause)