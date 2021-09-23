package com.blr19c.portal.base.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

/**
 * url路径转为<img src="url"></img>标签
 */
open class ConvertUrlPathToImgTagSerializer : JsonSerializer<Any?>() {

    override fun serialize(value: Any?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value == null) return
        gen.writeObject("<img src='$value'/>")
    }
}