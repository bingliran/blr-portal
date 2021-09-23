package com.blr19c.portal.tripartite.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Tripartite配置
 */
@Configuration
class TripartiteConfig {

    @Bean
    fun tripartiteInstantiationAwareBeanPostProcessor(): TripartiteInstantiationAwareBeanPostProcessor {
        return TripartiteInstantiationAwareBeanPostProcessor()
    }

    @Bean
    fun tripartiteDataFetchersScheduledTasks(): TripartiteDataFetchersScheduledTasks {
        return TripartiteDataFetchersScheduledTasks()
    }
}