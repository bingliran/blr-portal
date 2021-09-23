package com.blr19c.portal.modules.frontpage.service.impl

import com.blr19c.portal.config.security.RolesEnum
import com.blr19c.portal.modules.common.entity.ModelItem
import com.blr19c.portal.modules.common.service.impl.ModelItemServiceImpl
import com.blr19c.portal.modules.frontpage.service.FrontPageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

/**
 * 首页(每日推荐) serviceImpl
 */
@Service
class FrontPageServiceImpl : ModelItemServiceImpl(), FrontPageService {

    /**
     * 首页列表 根据时间热度更新
     */
    override fun list(): Flux<*> {
        val sort = Sort.by(
                Sort.Direction.DESC,
                columnName(ModelItem::createTime),
                columnName((ModelItem::heat))
        )
        val pageRequest = PageRequest.of(0, 10, sort)
        return modelItemMapper!!.findByMediaUrlNotNullAndAuthLessThanEqual(RolesEnum.ROLE_GENERAL.code, pageRequest)
    }
}