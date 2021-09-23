package com.blr19c.portal.config.r2dbc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing

/**
 * r2dbc配置
 */
@Configuration
@EnableR2dbcAuditing
class R2dbcConfig {
    @Bean
    fun springSecurityAuditorAware(): SpringSecurityAuditorAware {
        return SpringSecurityAuditorAware()
    }
}