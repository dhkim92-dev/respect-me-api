package kr.respectme.file.port.`in`.events

import kr.respectme.file.port.`in`.events.event.FileUploadedEvent

interface LocalFileEventListenPort {

    fun handleFileUploadedEvent(event: FileUploadedEvent)
}