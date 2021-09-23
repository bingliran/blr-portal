package com.blr19c.portal.modules.frontpage.controller

import com.blr19c.portal.base.BaseController
import com.blr19c.portal.base.network.ResponseData.Companion.success
import com.blr19c.portal.modules.frontpage.service.FrontPageService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * 首页(每日推荐)
 */
@RequestMapping("/frontPage")
@RestController
class FrontPageController(private val frontPageService: FrontPageService) : BaseController() {
    /**
     * 首页列表
     */
    @PostMapping("/list")
    fun list(): Mono<*> {
        return frontPageService.list().collectList().map { t -> success(t) }
    }
}