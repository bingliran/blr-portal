package com.blr19c.portal.modules.media.controller

import com.blr19c.portal.base.BaseController
import com.blr19c.portal.modules.common.utils.TemporaryFileUtils
import com.blr19c.portal.modules.common.utils.TemporaryFileUtils.checkExists
import com.blr19c.portal.modules.media.entity.FileRange
import org.apache.tika.Tika
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

/**
 * 媒体文件Controller
 */
@RestController
@RequestMapping("/mediaDownload")
class MediaDownloadController : BaseController() {

    /**
     * 展示临时文件
     */
    @RequestMapping("/temporaryFile/{fileName}")
    fun exhibitTemporaryFile(@RequestHeader(name = HttpHeaders.RANGE, required = false) fileRange: FileRange?,
                             @PathVariable fileName: String): ResponseEntity<*> {
        checkExists(fileName)
        val file = TemporaryFileUtils.file(fileName)
        //如果没有range
        return fileRange?.responseEntity(file) ?: download(file)
    }

    private fun download(file: File): ResponseEntity<InputStreamResource> {
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${URLEncoder.encode(file.name, UTF_8.name())}")
                .contentType(MediaType.valueOf(Tika().detect(file)))
                .contentLength(file.length())
                .body(InputStreamResource(FileInputStream(file)))
    }
}