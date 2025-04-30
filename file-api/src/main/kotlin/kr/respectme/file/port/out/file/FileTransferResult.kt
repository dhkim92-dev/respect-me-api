package kr.respectme.file.port.out.file

interface FileTransferResult {

    fun getOrigin(): String

    fun getRoot(): String

    fun getPath(): String

    fun toURL(): String
}