package kr.respectme.file.common.utility

import kr.respectme.file.configs.CDNConfig
import org.springframework.stereotype.Service

@Service
class CDNAccessPointBuilder(private val config: CDNConfig) {

    fun buildStoragePath(rootDir: String, accessKey: String, fileFormat: String): String {
        return "${config.storageOrigin}/$rootDir/${accessKey[0]}/${accessKey[1]}/$accessKey.$fileFormat"
    }

    fun buildAccessUrl(rootDir: String, accessKey: String, fileFormat: String): String {
        return "${config.host}/$rootDir/${accessKey[0]}/${accessKey[1]}/$accessKey.$fileFormat"
    }

    fun extractStoredPath(url: String): String {
        val split = url.split("/")
        return split.subList(1, split.size).joinToString("/")
    }

    fun extractAccessKey(url: String): String {
        extractFileName(url).let {
            return it.substring(0, it.lastIndexOf("."))
        }
    }

    fun extractFileName(url: String): String {
        val split = url.split("/")
        return split[split.size - 1]
    }
}