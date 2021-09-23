package com.blr19c.portal.config.security

import org.springframework.security.access.prepost.PreAuthorize

/**
 * admin用户才可以访问的接口
 */
@Target(AnnotationTarget.FUNCTION)
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@Retention(AnnotationRetention.RUNTIME)
annotation class AdminInterface 