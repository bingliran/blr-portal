package com.blr19c.portal.modules.welfare.controller

import com.blr19c.portal.base.BaseController
import com.blr19c.portal.base.network.RequestData
import com.blr19c.portal.base.network.ResponseData
import com.blr19c.portal.config.security.VipInterface
import com.blr19c.portal.modules.welfare.service.WelfareService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * 福利内容
 */
@RestController
@RequestMapping("/welfare")
class WelfareController(private val welfareService: WelfareService) : BaseController() {

    /**
     * 列表查询
     */
    @VipInterface
    @PostMapping("/list")
    fun list(@RequestBody requestData: RequestData): Mono<*> {
        return welfareService.list(requestData.getPage(), requestData.getDataNonNull())
                .collectList()
                .map { t -> ResponseData.success(t) }
    }
}