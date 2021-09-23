package com.blr19c.portal.config.security

import org.springframework.security.access.prepost.PreAuthorize

/**
 * vip用户才可以访问的接口
 */
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyAuthority('ROLE_VIP')")
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class VipInterface
