package com.blr19c.portal.config.web.pre.submit

import com.blr19c.portal.base.exception.BaseException

/**
 * 重复提交异常
 */
class PreventDuplicateSubmitException : BaseException {
    constructor() : super("请勿重复提交")
    constructor(second: Int) : super("请勿重复提交,在${second}秒后重试")
    constructor(cause: Throwable?) : super("请勿重复提交", cause)
}