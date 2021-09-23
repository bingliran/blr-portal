package com.blr19c.portal.base.redis

/**
 * redis限定名
 */
interface RedisQualifiedNameInterface {

    fun qualifiedName(): String

    fun getRedisKey(key: String): String {
        return "${qualifiedName()}::${key}"
    }
}