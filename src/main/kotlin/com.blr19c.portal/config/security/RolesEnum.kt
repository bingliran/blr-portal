package com.blr19c.portal.config.security

import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * 权限枚举
 */
enum class RolesEnum(val code: Int) {

    /**
     * 未登录用户
     */
    ROLE_ANONYMOUS(-99),

    /**
     * 普通用户
     */
    ROLE_GENERAL(0),

    /**
     * vip用户
     */
    ROLE_VIP(1),

    /**
     * 临时vip
     */
    ROLE_TEMPORARY_VIP(2),

    /**
     * 管理员用户
     */
    ROLE_ADMIN(99);

    /**
     * RolesEnum转为SimpleGrantedAuthority
     */
    fun grantedAuthority(): SimpleGrantedAuthority {
        return SimpleGrantedAuthority(this.name)
    }

    /**
     * 是否为特权用户
     */
    fun isPrivilegedUser(): Boolean {
        return this != ROLE_GENERAL && this != ROLE_ANONYMOUS
    }
}