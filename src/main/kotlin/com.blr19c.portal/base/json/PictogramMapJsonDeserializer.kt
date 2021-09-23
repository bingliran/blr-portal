package com.blr19c.portal.base.json

import com.blr19c.common.collection.PictogramMap
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * map转换为PictogramMap
 */
open class PictogramMapJsonDeserializer : JsonDeserializer<PictogramMap>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): PictogramMap? {
        val data = p?.readValueAs(HashMap::class.java) ?: return null
        return PictogramMap.toPictogramMap(data)
    }
}