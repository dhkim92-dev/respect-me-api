package kr.respectme.file.adapter.`in`.events

import kr.respectme.file.port.`in`.events.LocalFileEventListenPort
import kr.respectme.file.port.`in`.events.event.FileUploadedEvent
import kr.respectme.file.port.out.file.FileTransferService
import kr.respectme.file.port.out.file.TransferManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LocalFileEventListenAdapter(
    private val fileTransferService: FileTransferService
): LocalFileEventListenPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    override fun handleFileUploadedEvent(event: FileUploadedEvent) {
        try {
            fileTransferService.delete(event.fullPath)
        } catch(e: Exception) {
            logger.error("트랜잭션이 실패한 파일이 삭제되지 않았습니다. fullPath : ${event.fullPath}")
        }
    }
}