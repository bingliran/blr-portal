package com.blr19c.portal.base.network

import com.blr19c.portal.base.exception.BaseException
import com.blr19c.portal.base.json.LocalDateTimeJsonSerializer
import com.blr19c.portal.base.json.ResponseEnumJsonSerializer
import com.blr19c.common.spring.SpringBeanUtils
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.http.MediaType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.IOException
import java.io.OutputStream
import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.servlet.http.HttpServletResponse

/**
 * 通用返回参数
 */
open class ResponseData<T> : Serializable {

    /**
     * 返回状态值
     */
    @JsonSerialize(using = ResponseEnumJsonSerializer::class)
    var code: ResponseEnum? = null

    /**
     * 返回数据
     */
    var data: T? = null

    /**
     * 消息
     */
    var message: String? = null

    /**
     * 当前时间
     */
    @JsonSerialize(using = LocalDateTimeJsonSerializer::class)
    var currentTime: LocalDateTime? = null

    private constructor(code: ResponseEnum, data: T?, message: String?) {
        this.code = code
        this.data = data
        this.message = message
        this.currentTime = LocalDateTime.now()
    }


    private constructor(data: T?) {
        this.data = data
    }

    /**
     * 转为Mono
     */
    fun mono(): Mono<ResponseData<T>> {
        return Mono.just(this)
    }

    /**
     * 转为Flux
     */
    fun flux(): Flux<ResponseData<T>> {
        return mono().flux()
    }

    /**
     * 转为 void
     */
    fun nothing(): ResponseData<out Nothing> {
        val responseData = ResponseData(this.code!!, null, this.message)
        responseData.currentTime = this.currentTime
        return responseData
    }

    /**
     * 转为bytes
     */
    open fun bytes(): ByteArray {
        try {
            if (SpringBeanUtils.containsBean("objectMapper")) {
                return (SpringBeanUtils.getBean("objectMapper", ObjectMapper::class) as ObjectMapper)
                        .writeValueAsString(this).toByteArray(StandardCharsets.UTF_8)
            }
            return ObjectMapper().findAndRegisterModules().writeValueAsString(this).toByteArray(StandardCharsets.UTF_8)
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException(e)
        }
    }

    /**
     * 写出
     */
    open fun write(out: OutputStream) {
        try {
            out.write(this.bytes())
        } catch (e: IOException) {
            throw IllegalArgumentException(e)
        }
    }

    /**
     * 写出
     */
    open fun write(response: HttpServletResponse) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        write(response.outputStream)
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic
        fun <T> just(responseEnum: ResponseEnum, data: T?, message: String): ResponseData<T> {
            return ResponseData(responseEnum, data, message)
        }

        @JvmStatic
        fun <T> just(responseEnum: ResponseEnum): ResponseData<T> {
            return just(responseEnum, null, responseEnum.message)
        }

        @JvmStatic
        fun <T> success(data: T?): ResponseData<T> {
            return just(ResponseEnum.SUCCESS, data, ResponseEnum.SUCCESS.message)
        }

        @JvmStatic
        fun <T> success(): ResponseData<T> {
            return success(null)
        }

        @JvmStatic
        fun fail(message: String): ResponseData<Any> {
            return just(ResponseEnum.FAIL, null, message)
        }

        @JvmStatic
        fun fail(e: Throwable): ResponseData<Any> {
            if (e is BaseException) {
                return fail(e.message ?: ResponseEnum.FAIL.message)
            }
            return fail(ResponseEnum.FAIL.message)
        }

        @JvmStatic
        fun fail(): ResponseData<Any> {
            return fail(ResponseEnum.FAIL.message)
        }

    }
}