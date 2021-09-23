package com.blr19c.portal.base

import com.blr19c.common.spring.SpringBeanUtils
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import java.util.*
import kotlin.reflect.KMutableProperty1


/**
 * 顶级service
 */
interface BaseService {

    /**
     * kotlin通过function获取数据库字段名称
     */
    fun <T> columnName(property1: KMutableProperty1<T, *>): String {
        var name = property1.name
        if (name.length == 1 || (name.length > 1 && !Character.isUpperCase(name[1]))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1)
        }
        return name
    }

    /**
     * 获取R2dbcEntityOperations
     */
    fun r2dbcEntityOperations(): R2dbcEntityOperations {
        return SpringBeanUtils.getBean(R2dbcEntityOperations::class.java)
    }

    /**
     * 获取R2dbcEntityTemplate
     */
    fun r2dbcEntityTemplate(): R2dbcEntityTemplate {
        return SpringBeanUtils.getBean(R2dbcEntityTemplate::class.java)
    }
}