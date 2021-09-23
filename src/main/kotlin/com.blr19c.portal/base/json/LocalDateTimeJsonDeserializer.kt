package com.blr19c.portal.base.json

import com.blr19c.portal.base.exception.MissingParametersException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 时间戳转LocalDateTime
 */
open class LocalDateTimeJsonDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
        val time = p?.readValueAs(Long::class.java) ?: throw MissingParametersException("请求未携带时间戳")
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
    }
}