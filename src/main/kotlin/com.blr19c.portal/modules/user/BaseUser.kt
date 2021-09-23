package com.blr19c.portal.modules.user

import org.springframework.data.annotation.Id
import java.io.Serializable

/**
 * 用于security的user
 */
open class BaseUser : Serializable {
    /**
     * 用户唯一id
     */
    @Id
    var id: Long? = null
}