package com.blr19c.portal.modules.welfare.service

import com.blr19c.portal.base.network.Page
import com.blr19c.portal.modules.common.service.ModelItemService
import com.blr19c.common.collection.PictogramMap
import reactor.core.publisher.Flux

/**
 * 福利内容service
 */
interface WelfareService : ModelItemService {

    /**
     * 福利列表查询
     */
    fun list(page: Page, map: PictogramMap): Flux<*>

}