package com.blr19c.portal.tripartite.common

import com.blr19c.portal.base.log.ProtectedLogger
import com.blr19c.portal.config.security.SecurityConstant.SYSTEM_USER
import com.blr19c.portal.modules.common.entity.ModelItem
import com.blr19c.portal.modules.common.mapper.ModelItemMapper
import com.blr19c.portal.modules.common.utils.TemporaryFileUtils
import com.blr19c.common.spring.SpringBeanUtils
import org.springframework.data.domain.Example
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 第三方数据获取器
 * 准备执行参数 ExecuteParameter
 */
abstract class AbstractDataFetchers : ProtectedLogger() {

    companion object {
        val pool: ExecutorService = Executors.newSingleThreadExecutor()
    }

    /**
     * 获取ModelItem列表
     */
    abstract fun getItemList(): MutableList<ModelItem>

    /**
     * 数据链接
     */
    abstract fun datasourceUrl(): String

    /**
     * 保存数据
     */
    fun saveData() {
        val modelItemMapper: ModelItemMapper = SpringBeanUtils.getBean(ModelItemMapper::class.java)
        val list = getItemList()
        val modelItem = ModelItem()
        modelItem.dataSource = datasourceName()
        modelItem.moduleId = modelName()
        val oldList = modelItemMapper.findAll(Example.of(modelItem))
                .map { m -> m.dataSourceId!! }
                .collectList()
                .block()!!
        list.removeIf { m -> oldList.contains(m.dataSourceId) }
        for (mi in list) {
            mi.createUser = SYSTEM_USER.id
            mi.createTime = LocalDateTime.now()
            mi.lastUpdateTime = mi.createTime
        }
        if (list.isNotEmpty())
            modelItemMapper.saveAll(list).buffer().single().block()
    }

    /**
     * 下载解析文件
     */
    protected fun downloadFile(url: String, fileName: String) {
        pool.execute {
            try {
                if (!TemporaryFileUtils.exists(fileName))
                    TemporaryFileUtils.upload(url, fileName)
            } catch (e: Exception) {
                log.error("${fileName}:文件下载失败", e)
            }
        }
    }

    /**
     * 获取模块名称
     */
    protected fun modelName(): String {
        val name = this.javaClass.name
        val lastIndexOf = name.lastIndexOf(".")
        return name.substring(name.lastIndexOf(".", lastIndexOf - 1) + 1, lastIndexOf)
    }

    /**
     * 数据来源名称
     */
    protected fun datasourceName(): String {
        val name = this.javaClass.name
        val lastIndexOf = name.lastIndexOf(modelName()) - 1
        return name.substring(name.lastIndexOf(".", lastIndexOf - 1) + 1, lastIndexOf)
    }

    /**
     * 获取body中的url
     */
    protected fun urlMatcher(body: String): Matcher {
        return Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;()]+[-A-Za-z0-9+&@#/%=~_|]").matcher(body)
    }
}