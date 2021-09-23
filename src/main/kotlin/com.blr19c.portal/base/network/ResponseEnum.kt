package com.blr19c.portal.base.network

/**
 * response返回值 internal error
 */
enum class ResponseEnum(val code: String, val message: String) {
    SUCCESS("000000", "操作成功"),
    FAIL("100001", "操作失败"),
    NO_PERMISSION("100002", "无权限访问"),
    NO_HANDLER_FOUND("100003", "未找到路径"),
    INCORRECT_ACCOUNT_PASSWORD("100004", "用户名或密码错误"),
    LOGIN_INVALID("100005", "登录失效"),
    SERVER_ERROR("999999", "服务器错误,请稍后再试")
}