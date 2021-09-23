package com.blr19c.portal.modules.common.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 展示模块中的每一个子项(ModelItem)实体类
 *
 * @author blr
 * @since 2021-08-19 16:49:27
 */
@Table("model_item")
data class ModelItem(
        /**
         * 唯一id
         */
        @Id
        var id: Long? = null,

        /**
         * 标题
         */
        var title: String? = null,

        /**
         * 可见权限
         */
        var auth: Int? = null,

        /**
         * 所属模块id
         */
        var moduleId: String? = null,

        /**
         * 列表中的缩略图地址
         */
        //@JsonSerialize(using = ConvertUrlPathToImgTagSerializer::class)
        var thumbnailUrl: String? = null,

        /**
         * 媒体文件链接(如果存在)
         */
        @JsonSerialize
        var mediaUrl: String? = null,

        /**
         * 内容
         */
        var content: String? = null,

        /**
         * 数据来源
         */
        var dataSource: String? = null,

        /**
         * 数据来源id
         */
        var dataSourceId: String? = null,

        /**
         * 热度(点击数)
         */
        var heat: Int = 0,

        /**
         * 创建人id
         */
        @CreatedBy
        var createUser: Long? = null,

        /**
         * 创建时间
         */
        @CreatedDate
        @JsonFormat(pattern = "yyyy-MM-dd")
        var createTime: LocalDateTime? = null,

        /**
         * 最后修改时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd")
        var lastUpdateTime: LocalDateTime? = null
) : Serializable {


    companion object {
        private const val serialVersionUID = 756453988667931360L
    }
}