package com.blr19c.portal.modules.common.mapper

import com.blr19c.portal.modules.common.entity.ModelItem
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

/**
 * 展示模块mapper
 */
@Repository
interface ModelItemMapper : R2dbcRepository<ModelItem, Long> {

    /**
     * 查询mediaUrl不是null并且auth<=:auth的数据
     */
    fun findByMediaUrlNotNullAndAuthLessThanEqual(auth: Int, pageable: Pageable): Flux<ModelItem>

    /**
     * 根据auth查询
     */
    fun findByAuth(auth: Int, pageable: Pageable): Flux<ModelItem>

}