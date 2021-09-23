package com.blr19c.portal.config.security

import com.blr19c.portal.base.network.ResponseData
import com.blr19c.portal.base.network.ResponseEnum
import com.blr19c.portal.config.security.NetworkLimitConfig.networkLimitFilter
import com.blr19c.portal.config.security.TokenConfig.tokenConfig
import com.blr19c.portal.config.security.TokenConfig.tokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * security权限配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    /**
     * 密码编码器
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * security配置
     */
    @Override
    override fun configure(http: HttpSecurity) {
        val loginUrl = "/user/foreground/login"
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()//跨域
                .antMatchers(*whitelist()).permitAll() //白名单列表
                .anyRequest().authenticated() //所有请求都需要认证
                .and().addFilter(tokenFilter(authenticationManager())).apply(tokenConfig(loginUrl))//登录
                .and().logout() //使用/logout路径退出登录
                .and().csrf().disable()//禁用csrf保护
                .cors().configurationSource(corsConfigSource()) //跨域请求
                .and().addFilter(networkLimitFilter())//限流配置
                .exceptionHandling()//异常状态码处理
                .authenticationEntryPoint(LoginInvalidAuthenticationEntryPoint())//登录失效
                .accessDeniedHandler(RolesAccessDeniedHandler())//403
    }

    /**
     * 白名单接口
     */
    private fun whitelist(): Array<String> {
        return arrayOf(
                "/user/foreground/register/*",//注册接口白名单
                "/frontPage/*",//首页
                "/mediaDownload/**"//文件下载
        )
    }

    /**
     * 跨域
     */
    private fun corsConfigSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }

    /**
     * 403
     */
    class RolesAccessDeniedHandler : AccessDeniedHandler {
        @Override
        override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {
            response?.let { ResponseData.just<Any>(ResponseEnum.NO_PERMISSION).write(response) }
        }
    }

    /**
     * 登录失效
     */
    class LoginInvalidAuthenticationEntryPoint : AuthenticationEntryPoint {
        @Override
        override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
            response?.let { ResponseData.just<Any>(ResponseEnum.LOGIN_INVALID).write(response) }
        }
    }
}