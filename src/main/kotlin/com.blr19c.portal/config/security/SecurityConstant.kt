package com.blr19c.portal.config.security

import com.blr19c.portal.modules.user.admin.entity.AdminUser
import com.blr19c.common.code.SnowflakeIdUtils
import java.time.LocalDateTime

/**
 * 常量
 */
object SecurityConstant {

    /**
     * 永不过期
     */
    val NEVER_EXPIRE = LocalDateTime.of(2099, 1, 1, 0, 0).withNano(0)!!

    /**
     * 默认系统用户
     */
    val SYSTEM_USER = AdminUser(0, "system", "系统管理员", "bingliran@126.com", SnowflakeIdUtils.defaultNextId().toString(), LocalDateTime.now())
}