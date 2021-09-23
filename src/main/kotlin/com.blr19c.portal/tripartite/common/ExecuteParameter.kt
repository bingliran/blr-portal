package com.blr19c.portal.tripartite.common

import java.time.LocalDateTime

data class ExecuteParameter(
        /**
         * 发起时间
         */
        val currentTime: LocalDateTime,
        /**
         * 限制条数
         */
        val limit: Int,
        /**
         * 最小截至时间
         */
        val minTime: LocalDateTime
)
