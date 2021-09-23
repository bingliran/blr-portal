package com.blr19c.portal.base.exception

/**
 * 请求缺失必要参数
 */
open class MissingParametersException : BaseException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}