package com.blr19c.portal.modules.media.entity

import com.google.common.base.Strings
import org.apache.commons.io.input.RandomAccessFileInputStream
import org.apache.tika.Tika
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.DigestUtils
import java.io.File
import java.io.RandomAccessFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Instant

/**
 * 文件片段范围
 */
open class FileRange {

    /**
     * 起始
     */
    private val firstPos: Long

    /**
     * 截至
     */
    private val lastPos: Long?

    /**
     * 原有的range
     */
    private val range: String?

    constructor(range: String?) {
        this.range = range
        if (range == null) {
            this.firstPos = 0
            this.lastPos = null
            return
        }
        val split = range.split("bytes=|-".toRegex())
        this.firstPos = split[split.size - 2].toLong()
        this.lastPos = Strings.emptyToNull(split[split.size - 1])?.toLong() ?: firstPos + 1024000
    }

    private constructor(lowerBound: Long, upperBound: Long?) :
            this("bytes=${lowerBound}-${Strings.nullToEmpty(upperBound?.toString())}")

    /**
     * availableLong
     */
    private fun contentLength(length: Long): Long {
        return rangeEnd(length) - rangeStart() + 1
    }

    /**
     * [org.springframework.http.HttpRange.ByteRange.getRangeStart]
     */
    fun rangeStart(): Long {
        return firstPos
    }

    /**
     * [org.springframework.http.HttpRange.ByteRange.getRangeEnd]
     */
    fun rangeEnd(length: Long): Long {
        return if (lastPos != null && lastPos < length) {
            lastPos
        } else {
            length - 1
        }
    }

    /**
     * file转换为只有range可见长的ResponseEntity并生成默认的etag和lastModified
     */
    fun responseEntity(file: File): ResponseEntity<InputStreamResource> {
        return responseEntity(
                file,
                DigestUtils.md5DigestAsHex("${file.length()}${file.name}".encodeToByteArray()),
                Instant.ofEpochMilli(file.lastModified())
        )
    }

    /**
     * file转换为只有range可见长的ResponseEntity
     */
    fun responseEntity(file: File, eTag: String, lastModified: Instant): ResponseEntity<InputStreamResource> {
        val inputStream = FileResourceRegionInputStream(file, this)
        val length = file.length()
        val fileName = URLEncoder.encode(file.name, StandardCharsets.UTF_8.name())
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentLength(contentLength(length))
                .contentType(MediaType.valueOf(Tika().detect(file)))
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=${fileName}")
                .header(HttpHeaders.CONTENT_RANGE, "bytes ${inputStream.position}-${inputStream.available}/${length}")
                .lastModified(lastModified)
                .eTag(eTag)
                .body(InputStreamResource(inputStream))
    }

    /**
     * 仅有range可见的RandomAccessFileInputStream
     */
    internal class FileResourceRegionInputStream(file: File, fileRange: FileRange) : RandomAccessFileInputStream(RandomAccessFile(file, "r")) {

        val available: Long = fileRange.rangeEnd(file.length())

        @Volatile
        var position: Long = skip(fileRange.rangeStart())

        init {
            randomAccessFile.seek(position)
        }

        @Synchronized
        override fun read(): Int {
            if (checkFilePointer()) return -1
            position--
            return super.read()
        }

        @Synchronized
        override fun availableLong(): Long {
            return available - position
        }

        @Synchronized
        override fun read(bytes: ByteArray, offset: Int, length: Int): Int {
            var len = length
            if (checkFilePointer()) return -1
            len = availableLong().coerceAtMost(len.toLong()).toInt()
            position -= len.toLong()
            return super.read(bytes, offset, len)
        }

        @Synchronized
        override fun read(bytes: ByteArray): Int {
            return this.read(bytes, 0, bytes.size)
        }

        @Synchronized
        private fun checkFilePointer(): Boolean {
            return availableLong() == 0L
        }

    }

    companion object {
        @JvmStatic
        fun of(firstPos: Long, lastPos: Long?): FileRange {
            return FileRange(firstPos, lastPos)
        }
    }
}