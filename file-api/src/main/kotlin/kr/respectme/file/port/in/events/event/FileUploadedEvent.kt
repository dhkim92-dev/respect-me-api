package kr.respectme.file.port.`in`.events.event

import org.springframework.context.ApplicationEvent

class FileUploadedEvent(source: Any,
    val fullPath: String
): ApplicationEvent(source) {
}