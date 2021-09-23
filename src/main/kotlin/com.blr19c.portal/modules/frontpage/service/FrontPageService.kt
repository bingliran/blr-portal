package com.blr19c.portal.modules.frontpage.service

import com.blr19c.portal.modules.common.service.ModelItemService
import reactor.core.publisher.Flux

/**
 * 首页(每日推荐) service
 */
interface FrontPageService : ModelItemService {

    /**
     * 首页列表 根据热度更新
     */
    fun list(): Flux<*>
}