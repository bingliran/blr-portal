package com.blr19c.portal.config.security

import com.blr19c.portal.base.exception.MissingParametersException
import com.blr19c.portal.base.network.RequestData
import com.blr19c.portal.base.network.ResponseData
import com.blr19c.portal.base.network.ResponseEnum
import com.blr19c.portal.base.redis.RedisKeyUtils
import com.blr19c.common.code.SnowflakeIdUtils
import com.blr19c.common.spring.SpringBeanUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.boot.autoconfigure.session.SessionProperties
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * session token配置
 */
object TokenConfig {

    /**
     * 获取tokenFilter
     */
    fun tokenFilter(auth: AuthenticationManager): TokenAuthenticationFilter {
        return TokenAuthenticationFilter(auth)
    }

    /**
     * 获取tokenConfig
     */
    fun <H : HttpSecurityBuilder<H>> tokenConfig(loginUrl: String): JsonTokenLoginConfigurer<H> {
        return JsonTokenLoginConfigurer(AccountPasswordTokenAuthenticationFilter(loginUrl))
    }

    /**
     * token过滤生成AccountPasswordAuthenticationToken
     */
    class TokenAuthenticationFilter(authenticationManager: AuthenticationManager) : BasicAuthenticationFilter(authenticationManager) {

        @Suppress("unchecked_cast")
        override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
            // token置于header里
            val token = request.getHeader("Token") ?: request.getParameter("Token")
            if (StringUtils.isNotBlank(token)) {
                val redisTemplate = SpringBeanUtils.getBean("redisTemplate") as RedisTemplate<Any, Any>
                val key = RedisKeyUtils.getKey(token, TokenConfig::class.java)
                val authentication = redisTemplate.opsForValue().get(key) as Authentication?
                authentication?.let {
                    SecurityUtils.setAuthentication(authentication)
                    //重置过期时间
                    redisTemplate.expire(key, SpringBeanUtils.getBean(SessionProperties::class.java).timeout)
                }
            }
            return super.doFilterInternal(request, response, chain)
        }
    }

    /**
     * 登录成功/失败返回authentication
     */
    object TokenAuthenticationHandler : AuthenticationSuccessHandler, AuthenticationFailureHandler {

        @Suppress("unchecked_cast")
        override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication) {
            //see AccountPasswordTokenAuthenticationFilter.attemptAuthentication
            authentication as UsernamePasswordAuthenticationToken
            val auth = AccountPasswordAuthenticationToken(
                    authentication.principal,
                    authentication.credentials,
                    SnowflakeIdUtils.defaultNextId().toString(),
                    authentication.authorities
            )
            val redisTemplate = SpringBeanUtils.getBean("redisTemplate") as RedisTemplate<Any, Any>
            val timeout = SpringBeanUtils.getBean(SessionProperties::class.java).timeout
            //设置token
            redisTemplate.opsForValue().set(RedisKeyUtils.getKey(auth.token, TokenConfig::class.java), auth, timeout)
            response?.let { ResponseData.success(auth.copy()).write(response) }
        }

        override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse?, exception: AuthenticationException?) {
            response?.let { ResponseData.just<Any>(ResponseEnum.INCORRECT_ACCOUNT_PASSWORD).write(response) }
        }
    }

    /**
     * jsonToken登录
     */
    class JsonTokenLoginConfigurer<H : HttpSecurityBuilder<H>>(private val authFilter: AccountPasswordTokenAuthenticationFilter) :
            AbstractAuthenticationFilterConfigurer<H, JsonTokenLoginConfigurer<H>, AccountPasswordTokenAuthenticationFilter>
            (authFilter, authFilter.defaultFilterProcessesUrl) {
        /**
         * 重新设置登录成功/失败返回结果
         */
        override fun init(http: H) {
            successHandler(TokenAuthenticationHandler)
            failureHandler(TokenAuthenticationHandler)
            loginPage(authFilter.defaultFilterProcessesUrl)
            super.init(http)
        }

        override fun createLoginProcessingUrlMatcher(loginProcessingUrl: String?): RequestMatcher {
            return AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name)
        }
    }


    /**
     * 账号密码token权限认证过滤器
     */
    class AccountPasswordTokenAuthenticationFilter(val defaultFilterProcessesUrl: String) : UsernamePasswordAuthenticationFilter() {

        init {
            //重新设置路径
            setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.name))
            setAuthenticationSuccessHandler(TokenAuthenticationHandler)
            setAuthenticationFailureHandler(TokenAuthenticationHandler)
        }

        override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication? {
            if (request.method != HttpMethod.POST.name) {
                throw MissingParametersException("仅支持post请求")
            }
            val map = RequestData.of(request).getDataNonNull()
            val authRequest = UsernamePasswordAuthenticationToken(
                    map.getString("account", ""),
                    map.getString("password", "")
            )
            authRequest.details = authenticationDetailsSource.buildDetails(request)
            return authenticationManager.authenticate(authRequest)
        }
    }

    /**
     * 带有token的认证信息
     */
    class AccountPasswordAuthenticationToken(account: Any?,
                                             password: Any?,
                                             val token: String,
                                             authorities: Collection<GrantedAuthority>?
    ) : UsernamePasswordAuthenticationToken(account, password, authorities) {

        companion object {
            const val serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID
        }

        fun copy(): AccountPasswordAuthenticationToken {
            return AccountPasswordAuthenticationToken(
                    null, credentials, token, authorities
            )
        }
    }
}