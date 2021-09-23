package com.blr19c.portal.modules.user.foreground.exception

import com.blr19c.portal.base.exception.BaseException

/**
 * 账号已存在异常
 */
class AccountAlreadyException : BaseException {
    constructor(account: String) : super("账号:$account 已存在")
    constructor(account: String, cause: Throwable?) : super("账号:$account 已存在", cause)
}