package com.blr19c.portal.base.redis

object RedisKeyUtils {

    @JvmStatic
    fun getKey(key: String, clazz: Class<*>): String {
        return getKey(key, ClassRedisQualifiedName(clazz))
    }

    @JvmStatic
    fun getKey(key: String, redisQualifiedNameInterface: RedisQualifiedNameInterface): String {
        return redisQualifiedNameInterface.getRedisKey(key)
    }
}