package com.blr19c.portal.modules.welfare.service.impl

import com.blr19c.portal.base.network.Page
import com.blr19c.portal.config.security.RolesEnum
import com.blr19c.portal.modules.common.entity.ModelItem
import com.blr19c.portal.modules.common.service.impl.ModelItemServiceImpl
import com.blr19c.portal.modules.welfare.service.WelfareService
import com.blr19c.common.collection.PictogramMap
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

/**
 * 福利内容serviceImpl
 */
@Service
class WelfareServiceImpl : ModelItemServiceImpl(), WelfareService {

    /**
     * 福利列表查询
     */
    override fun list(page: Page, map: PictogramMap): Flux<*> {
        val sort = Sort.by(
                Sort.Direction.DESC,
                columnName(ModelItem::createTime)
        )
        return modelItemMapper!!.findByAuth(RolesEnum.ROLE_VIP.code, PageRequest.of(page.page, page.size, sort))
    }
}