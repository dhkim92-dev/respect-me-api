package kr.respectme.file.adapter.out.file.dto

import kr.respectme.file.port.out.file.FileTransferResult
import software.amazon.awssdk.services.s3.model.PutObjectResponse

class S3FileTransferResult(
    private val region: String,
    private val bucket: String,
    private val root: String,
    private val path: String
): FileTransferResult {

    override fun getOrigin(): String {
        return bucket
    }

    override fun getRoot(): String {
        return root
    }

    override fun getPath(): String {
        return path
    }

    override fun toURL(): String {
        return "https://$bucket.s3.$region.amazonaws.com/$path"
    }

    companion object {
        fun of(bucket: String,
               root: String,
               region: String,
               path: String): S3FileTransferResult {

            return S3FileTransferResult(
                region = region,
                bucket = bucket,
                root = root,
                path = path
            )
        }
    }
}