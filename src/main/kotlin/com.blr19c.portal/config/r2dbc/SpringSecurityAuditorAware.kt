package com.blr19c.portal.config.r2dbc

import com.blr19c.portal.modules.user.BaseUser
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono

/**
 * [org.springframework.data.annotation.CreatedBy] 使用security的user
 */
class SpringSecurityAuditorAware : ReactiveAuditorAware<BaseUser> {
    override fun getCurrentAuditor(): Mono<BaseUser> {
        return ReactiveSecurityContextHolder.getContext()
                .map { obj -> obj.authentication }
                .filter { obj -> obj.isAuthenticated }
                .map { obj -> obj.principal }
                .map { obj -> BaseUser::class.java.cast(obj) }
    }
}