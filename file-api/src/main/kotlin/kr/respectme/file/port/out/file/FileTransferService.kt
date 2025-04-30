package kr.respectme.file.port.out.file

import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.io.InputStream

/**
 * FileTransferService interface
 * API 요청으로 받은 파일을 저장 시스템에 저장하거나 삭제하기 위한 인터페이스
 */
interface FileTransferService {

    /**
     * 파일을 저장 시스템에 저장하는 메서드
     * 저장 시스템에 정상 저장된 경우
     * @param inputStream File의 InputStream
     * @param origin 파일 저장 시스템의 최상위 루트
     * @param root 파일 저장 시스템에서 도메인 루트(예 /images)
     * @param path 도메인 루트 하위 접근 경로(예 /a/b/abcdef.jpeg)
     * @param size File의 크기
     * @return FileTransferResult
     */
    fun upload(inputStream: InputStream,
               contentType: String,
               origin: String,
               root: String,
               fileName: String,
               size: Long): FileTransferResult

    /**
     * 파일을 저장 시스템에서 삭제하는 메서드
     * @param origin 파일 저장 시스템의 최상위 루트
     * @param root 파일 저장 시스템에서 도메인 루트(예 /images)
     * @param path 도메인 루트 하위 접근 경로(예 /a/b/abcdef.jpeg)
     */
    fun delete(url: String): Boolean

    /**
     * CDN을 통해 접근할 수 있는 URL로 변환한다.
     * @param fullPath 도메인 루트를 포함한 파일 접근 경로
     * @return CDN 접근 URL
     */
    fun pathToCDNAccessURL(fullPath: String): String
}