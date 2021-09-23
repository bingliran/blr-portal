package com.blr19c.portal.base.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * LocalDateTime转时间戳
 */
open class LocalDateTimeJsonSerializer : JsonSerializer<LocalDateTime>() {

    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        val toEpochMilli = value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                ?: System.currentTimeMillis()
        gen?.writeObject(toEpochMilli)
    }
}