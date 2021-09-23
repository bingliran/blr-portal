package com.blr19c.portal.modules.user.foreground.entity

import com.blr19c.portal.config.security.RolesEnum
import com.blr19c.portal.config.security.SecurityConstant
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 前台用户角色表(ForegroundUserRole)实体类
 *
 * @author blr
 * @since 2021-08-12 09:29:10
 */
@Table("foreground_user_role")
class ForegroundUserRole : Serializable {

    /**
     * 主键
     */
    @Id
    var id: Long? = null

    /**
     * 前台用户id
     */
    var foregroundUserId: Long? = null

    /**
     * 角色名称
     */
    var roleName: String? = null

    /**
     * 创建时间
     */
    @CreatedDate
    var createTime: LocalDateTime? = null

    /**
     * 到期时间
     */
    var expireTime: LocalDateTime? = null

    constructor(foregroundUserId: Long?, roleName: String?, createTime: LocalDateTime?, expireTime: LocalDateTime?) {
        this.foregroundUserId = foregroundUserId
        this.roleName = roleName
        this.createTime = createTime
        this.expireTime = expireTime
    }

    constructor(foregroundUserId: Long?) {
        this.foregroundUserId = foregroundUserId
    }

    @PersistenceConstructor
    constructor()

    companion object {
        private const val serialVersionUID = 105952359526304256L

        /**
         * 默认的用户权限
         */
        @JvmStatic
        fun getDefaultRole(foregroundUserId: Long): ForegroundUserRole {
            return ForegroundUserRole(foregroundUserId, RolesEnum.ROLE_GENERAL.name, LocalDateTime.now(), SecurityConstant.NEVER_EXPIRE)
        }
    }
}