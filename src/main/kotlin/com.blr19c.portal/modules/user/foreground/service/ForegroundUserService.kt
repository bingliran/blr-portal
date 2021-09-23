package com.blr19c.portal.modules.user.foreground.service

import com.blr19c.portal.base.BaseService
import com.blr19c.portal.modules.user.foreground.entity.ForegroundUser
import com.blr19c.portal.modules.user.foreground.entity.ForegroundUserRole
import org.springframework.security.core.userdetails.UserDetailsService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 前台用户service
 */
interface ForegroundUserService : UserDetailsService, BaseService {

    /**
     * 新建用户
     */
    fun add(foregroundUser: ForegroundUser): Mono<*>

    /**
     * 根据account查询前台用户
     */
    fun findByAccount(account: String): Mono<ForegroundUser>

    /**
     * 根据前台用户id查询前台用户角色
     */
    fun findRoleById(id: Long): Flux<ForegroundUserRole>
}