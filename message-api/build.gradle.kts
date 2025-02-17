import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.bmuschko.docker-remote-api") version "9.3.1"
}

version = "0.3.0"

repositories {
    mavenCentral()
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":common"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.kafka:spring-kafka")
//    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // B3 Propagation 구현 라이브러리
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")

}

kapt {
    correctErrorTypes = true
}

tasks.register<DockerBuildImage>("buildTestImage") {
    dependsOn("bootJar")
    inputDir.set(file("."))
    images.add("elensar92/respect-me-message-api:${version}-test")
    group = "docker"
}

tasks.register<DockerPushImage>("pushTestImage") {
    dependsOn("buildTestImage")
    images.add("elensar92/respect-me-message-api:${version}-test")
    group = "docker"
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.message-api")
    archiveVersion.set("latest")
}

tasks.test {
    useJUnitPlatform()
}