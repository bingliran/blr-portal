package com.blr19c.portal.tripartite.config

import com.blr19c.portal.tripartite.common.AbstractDataFetchers
import com.blr19c.portal.tripartite.config.TripartiteDataFetchersHolder.addTripartiteDataFetchers
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor
import org.springframework.core.annotation.AnnotationUtils
import kotlin.reflect.full.cast

/**
 * 处理带有@TripartiteDataFetchers的实例
 */
class TripartiteInstantiationAwareBeanPostProcessor : InstantiationAwareBeanPostProcessor {

    @Throws(BeansException::class)
    override fun postProcessAfterInstantiation(bean: Any, beanName: String): Boolean {
        val annotation = AnnotationUtils.findAnnotation(bean.javaClass, TripartiteDataFetchers::class.java)
        if (annotation != null) {
            addTripartiteDataFetchers(beanName, AbstractDataFetchers::class.cast(bean))
        }
        return true
    }
}