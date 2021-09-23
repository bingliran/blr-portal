package com.blr19c.portal.tripartite.config

import com.blr19c.portal.base.log.ProtectedLogger
import com.blr19c.common.scheduled.EditableScheduled

/**
 * 获取第三方数据的计划任务
 */
class TripartiteDataFetchersScheduledTasks : ProtectedLogger() {

    /**
     * 每天0点执行获取第三方数据
     */
    @EditableScheduled(cron = "0 0 0 */1 * ?")
    fun run() {
        val fetchersList = TripartiteDataFetchersHolder.getTripartiteDataFetchersList()
        for (f in fetchersList) {
            try {
                f.saveData()
            } catch (e: Exception) {
                log.error("${f.javaClass.simpleName}获取第三方数据失败", e)
            }

        }
    }

}