package com.blr19c.portal.modules.common.utils

import org.apache.commons.io.FileUtils
import org.springframework.boot.system.ApplicationHome
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL

object TemporaryFileUtils {

    /**
     * 临时文件路径
     */
    private val TEMPORARY_FILE_PATH = (ApplicationHome(TemporaryFileUtils::class.java)
            .source
            .parentFile
            .parentFile
            .path
            + "/temporaryFile")

    /**
     * 上传临时文件
     */
    fun upload(url: String?, fileName: String) {
        FileUtils.copyURLToFile(URL(url), File("$TEMPORARY_FILE_PATH/$fileName"))
    }

    /**
     * 上传临时文件
     */
    fun upload(inputStream: InputStream?, fileName: String) {
        FileUtils.copyInputStreamToFile(inputStream, File("$TEMPORARY_FILE_PATH/$fileName"))
    }

    /**
     * 下载临时文件
     */
    fun download(fileName: String): InputStream {
        checkExists(fileName)
        return FileInputStream(file(fileName))
    }

    /**
     * 文件是否存在
     */
    fun exists(fileName: String): Boolean {
        return file(fileName).exists()
    }

    /**
     * 获取文件
     */
    fun file(fileName: String): File {
        return File("$TEMPORARY_FILE_PATH/$fileName")
    }

    /**
     * 验证文件是否存在
     */
    fun checkExists(fileName: String) {
        if (!exists(fileName)) {
            throw FileNotFoundException(fileName + "文件不存在")
        }
    }
}