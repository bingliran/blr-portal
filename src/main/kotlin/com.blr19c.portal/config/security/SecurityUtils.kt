package com.blr19c.portal.config.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

/**
 * security工具类
 */
object SecurityUtils {

    /**
     * 获取authentication
     */
    fun authentication(): Authentication? {
        return SecurityContextHolder.getContext().authentication
    }

    /**
     * 设置authentication
     */
    fun setAuthentication(authentication: Authentication) {
        SecurityContextHolder.getContext().authentication = authentication
    }

    /**
     * 当前authentication是否拥有特权
     */
    fun isPrivileged(): Boolean {
        return authentication()?.authorities?.stream()
                ?.anyMatch { a -> RolesEnum.valueOf(a.authority).isPrivilegedUser() } == true
    }
}