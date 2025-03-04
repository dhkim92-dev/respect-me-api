package kr.respectme.file.configs

import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OracleObjectStorageConfig(){

    @Bean
    fun oracleConfigFileProvider(): ConfigFileAuthenticationDetailsProvider {
        val provider = ConfigFileAuthenticationDetailsProvider("~/.oci/config", "DEFAULT")
        return provider
    }

    @Bean
    fun oracleStorageClient(provider: ConfigFileAuthenticationDetailsProvider): ObjectStorage {
        return ObjectStorageClient.builder()
            .build(provider)
    }
}