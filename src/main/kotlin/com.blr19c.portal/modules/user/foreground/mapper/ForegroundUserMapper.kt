package com.blr19c.portal.modules.user.foreground.mapper

import com.blr19c.portal.modules.user.foreground.entity.ForegroundUser
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

/**
 * 前台用户mapper
 */
@Repository
interface ForegroundUserMapper : R2dbcRepository<ForegroundUser?, Long?>