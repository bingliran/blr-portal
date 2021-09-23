package com.blr19c.portal.modules.user.foreground.service.impl

import com.blr19c.portal.base.exception.ServerInternalException
import com.blr19c.portal.modules.user.foreground.entity.ForegroundUser
import com.blr19c.portal.modules.user.foreground.entity.ForegroundUserRole
import com.blr19c.portal.modules.user.foreground.exception.AccountAlreadyException
import com.blr19c.portal.modules.user.foreground.exception.IncorrectUsernameOrPasswordException
import com.blr19c.portal.modules.user.foreground.mapper.ForegroundUserMapper
import com.blr19c.portal.modules.user.foreground.mapper.ForegroundUserRoleMapper
import com.blr19c.portal.modules.user.foreground.service.ForegroundUserService
import com.blr19c.common.code.SnowflakeIdUtils
import org.springframework.data.domain.Example
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.stream.Collectors

/**
 *  前台用户serviceImpl
 */
@Service
class ForegroundUserServiceImpl(
        private val foregroundUserMapper: ForegroundUserMapper,
        private val foregroundUserRoleMapper: ForegroundUserRoleMapper,
        private val passwordEncoder: PasswordEncoder) : ForegroundUserService {

    /**
     * 新建用户
     */
    @Override
    @Transactional
    override fun add(foregroundUser: ForegroundUser): Mono<*> {
        return foregroundUserMapper.exists(Example.of(ForegroundUser(foregroundUser.account)))
                .doOnSuccess { existsUser ->
                    if (existsUser)
                        throw AccountAlreadyException(foregroundUser.account!!)
                }
                .then(saveForegroundUser(foregroundUser))
                .then(saveDefaultForegroundUserRole(foregroundUser))
    }

    /**
     * 根据account查询前台用户
     */
    @Override
    override fun findByAccount(account: String): Mono<ForegroundUser> {
        return foregroundUserMapper.findOne(Example.of(ForegroundUser(account)))
    }

    /**
     * 根据前台用户id查询前台用户角色
     */
    @Override
    override fun findRoleById(id: Long): Flux<ForegroundUserRole> {
        return foregroundUserRoleMapper.findAll(Example.of(ForegroundUserRole(id)))
    }

    /**
     * 保存账号信息
     */
    private fun saveForegroundUser(foregroundUser: ForegroundUser): Mono<ForegroundUser> {
        foregroundUser.password = passwordEncoder.encode(foregroundUser.password)
        foregroundUser.createTime = LocalDateTime.now()
        foregroundUser.id = SnowflakeIdUtils.defaultNextId()
        return r2dbcEntityOperations().insert(foregroundUser)
    }

    /**
     * 添加默认权限
     */
    private fun saveDefaultForegroundUserRole(foregroundUser: ForegroundUser): Mono<ForegroundUserRole> {
        return foregroundUserRoleMapper.save(
                ForegroundUserRole.getDefaultRole(foregroundUser.id!!)
        )
    }

    /**
     * 根据account加载用户
     */
    @Override
    override fun loadUserByUsername(account: String?): UserDetails {
        if (account == null)
            throw IncorrectUsernameOrPasswordException()
        val optInfo = findByAccount(account).blockOptional()
        //未查询到
        if (!optInfo.isPresent)
            throw IncorrectUsernameOrPasswordException()
        //获取权限列表
        val roleList = findRoleById(optInfo.get().id ?: throw ServerInternalException())
                .toStream()
                .map { roleUser -> roleUser.roleName!! }
                .map { roleName -> SimpleGrantedAuthority(roleName) }
                .collect(Collectors.toList())
        return User(optInfo.get().account, optInfo.get().password, roleList)
    }
}