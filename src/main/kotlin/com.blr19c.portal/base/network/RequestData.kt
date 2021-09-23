package com.blr19c.portal.base.network

import com.blr19c.portal.base.exception.MissingParametersException
import com.blr19c.portal.base.json.LocalDateTimeJsonDeserializer
import com.blr19c.portal.base.json.PictogramMapJsonDeserializer
import com.blr19c.common.collection.PictogramMap
import com.blr19c.common.spring.SpringBeanUtils
import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.io.Serializable
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

/**
 * 通用请求参数
 */
open class RequestData : Serializable {

    companion object {

        init {
            //设置分页属性
            PictogramMap.setGlobalPageName("page", "size", true)
        }

        private const val serialVersionUID = 1L

        /**
         * request转为RequestData
         */
        fun of(request: HttpServletRequest): RequestData {
            try {
                if (SpringBeanUtils.containsBean("objectMapper")) {
                    return (SpringBeanUtils.getBean("objectMapper", ObjectMapper::class) as ObjectMapper)
                            .readValue(request.inputStream, RequestData::class.java)
                }
                return ObjectMapper().findAndRegisterModules().readValue(request.inputStream, RequestData::class.java)
            } catch (ex: JacksonException) {
                throw MissingParametersException("错误的请求参数", ex)
            }

        }
    }

    /**
     * 请求数据
     */
    @JsonDeserialize(using = PictogramMapJsonDeserializer::class)
    var data: PictogramMap? = null

    /**
     * 请求时间
     */
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer::class)
    var currentTime: LocalDateTime? = null

    /**
     * 转为指定类型的实体类
     */
    open fun <T> toModel(modelClass: Class<T>): T {
        return getDataNonNull().toModel(modelClass, { obj, key -> obj[key] }, true, false)
    }

    /**
     * 获取分页内容
     */
    open fun getPage(): Page {
        val map = getDataNonNull()
        return Page(map.pageNum - 1, map.pageSize)
    }

    /**
     * 获取非空的数据
     */
    open fun getDataNonNull(): PictogramMap {
        if (data == null)
            throw MissingParametersException("错误的请求参数")
        return data as PictogramMap
    }
}