package com.blr19c.portal.base.redis

/**
 * class名称的redis限定名
 */
class ClassRedisQualifiedName(private val clazz: Class<*>) : RedisQualifiedNameInterface {

    override fun qualifiedName(): String {
        return clazz.simpleName
    }
}