package com.blr19c.portal.modules.user.foreground.mapper

import com.blr19c.portal.modules.user.foreground.entity.ForegroundUserRole
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

/**
 * 前台用户角色mapper
 */
@Repository
interface ForegroundUserRoleMapper : R2dbcRepository<ForegroundUserRole?, Long?>