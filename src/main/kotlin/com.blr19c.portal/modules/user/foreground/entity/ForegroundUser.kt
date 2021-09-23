package com.blr19c.portal.modules.user.foreground.entity

import com.blr19c.portal.modules.user.BaseUser
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * 前台用户(ForegroundUser)实体类
 *
 * @author blr
 * @since 2021-08-10 15:33:10
 */
@Table("foreground_user")
open class ForegroundUser : BaseUser {

    /**
     * 账号
     */
    var account: String? = null

    /**
     * 昵称
     */
    var nickName: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 注册时间
     */
    @CreatedDate
    var createTime: LocalDateTime? = null

    @PersistenceConstructor
    constructor()

    constructor(account: String?) {
        this.account = account
    }

    constructor(account: String?, password: String?, createTime: LocalDateTime?) {
        this.account = account
        this.password = password
        this.createTime = createTime
    }

    constructor(account: String?, nickName: String?, email: String?, password: String?, createTime: LocalDateTime?) {
        this.account = account
        this.nickName = nickName
        this.email = email
        this.password = password
        this.createTime = createTime
    }


    companion object {
        private const val serialVersionUID = 481526520014639331L
    }
}