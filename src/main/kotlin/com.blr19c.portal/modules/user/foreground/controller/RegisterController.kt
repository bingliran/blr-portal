package com.blr19c.portal.modules.user.foreground.controller

import com.blr19c.portal.base.BaseController
import com.blr19c.portal.base.exception.MissingParametersException
import com.blr19c.portal.base.network.RequestData
import com.blr19c.portal.base.network.ResponseData.Companion.success
import com.blr19c.portal.base.redis.RedisKeyUtils
import com.blr19c.portal.config.web.pre.submit.PreventDuplicateSubmit
import com.blr19c.portal.config.web.pre.submit.RepeatVerifyFunctionConstant
import com.blr19c.portal.modules.user.foreground.entity.ForegroundUser
import com.blr19c.portal.modules.user.foreground.service.ForegroundUserService
import com.blr19c.common.mail.MailUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * 前台用户注册
 */
@RestController
@RequestMapping("/user/foreground/register")
class RegisterController(private val foregroundUserService: ForegroundUserService,
                         private val stringRedisTemplate: StringRedisTemplate) : BaseController() {

    /**
     * 新用户注册
     */
    @PostMapping("/registerUser")
    fun registerUser(@RequestBody requestData: RequestData): Mono<*> {
        val data = requestData.getDataNonNull()
        if (data.isBlank("account") || data.isBlank("password")) {
            throw MissingParametersException("请输入账号密码")
        }
        val account = data.getString("account")
        val password = data.getString("password")
        val nickName = data.getString("nickName")
        val email = data.getString("email")
        val foregroundUser = ForegroundUser()
        foregroundUser.account = account
        foregroundUser.password = password
        foregroundUser.nickName = nickName
        foregroundUser.email = email
        return foregroundUserService.add(foregroundUser).thenReturn(success<Any>())
    }

    /**
     * 邮件验证码
     */
    @PostMapping("/verificationCode")
    @PreventDuplicateSubmit(duration = "PT60S", repeatVerify = RepeatVerifyFunctionConstant.UseIP::class)
    fun verificationCode(@RequestBody requestData: RequestData): Mono<*> {
        val email = requestData.getDataNonNull().getString("email")
        if (StringUtils.isBlank(email)) {
            throw MissingParametersException("请输入邮箱账号")
        }
        val code = StringBuilder()
        var index = 4
        while (index-- > 0) code.append(Random.nextInt(10))
        stringRedisTemplate.opsForValue().set(RedisKeyUtils.getKey(email, this.javaClass), code.toString(), 10, TimeUnit.MINUTES)
        MailUtils.send("blr19c.com验证码", code.toString(), email)
        return success<Any>().mono()
    }
}