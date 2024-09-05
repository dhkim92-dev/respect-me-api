package kr.respectme.common.security.jwt.adapter

import kr.respectme.common.error.BusinessException
import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.JwtAuthenticationException
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class RestJwtAuthenticationAdapter(private val url: String): JwtAuthenticationPort {

    private val webClient = WebClient.create()
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun validate(jwtValidationRequest: JwtValidateRequest): ApiResult<Map<String, Any?>> {
        return webClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(jwtValidationRequest)
            .retrieve()
            .onStatus({ status -> status == HttpStatus.UNAUTHORIZED }, { response ->
                response.bodyToMono(String::class.java)
                    .doOnNext {
                        logger.error("RestJwtAuthenticationAdapter receive 401 response  $it")
                    }
                    .map { JwtAuthenticationException(it) }
            })
            .onStatus({status -> status.is5xxServerError}, { response ->
                response.bodyToMono(String::class.java)
                    .doOnNext {
                        logger.error("RestJwtAuthenticationAdapter receive 5xx response $it")
                    }
                    .map { BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", it) }
            })
            .bodyToMono(object: ParameterizedTypeReference<ApiResult<Map<String, Any?>>>(){})
            .block() ?: throw BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", "response is null")
    }
}