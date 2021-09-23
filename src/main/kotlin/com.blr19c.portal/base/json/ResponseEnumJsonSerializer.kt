package com.blr19c.portal.base.json

import com.blr19c.portal.base.network.ResponseEnum
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * ResponseEnum转为code
 */
open class ResponseEnumJsonSerializer : JsonSerializer<ResponseEnum>() {

    override fun serialize(value: ResponseEnum, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value.code)
    }
}