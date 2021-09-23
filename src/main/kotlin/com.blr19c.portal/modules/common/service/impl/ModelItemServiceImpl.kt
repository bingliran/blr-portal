package com.blr19c.portal.modules.common.service.impl

import com.blr19c.portal.modules.common.mapper.ModelItemMapper
import com.blr19c.portal.modules.common.service.ModelItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 展示模块serviceImpl
 */
@Service
class ModelItemServiceImpl : ModelItemService {

    @Autowired
    protected var modelItemMapper: ModelItemMapper? = null

}