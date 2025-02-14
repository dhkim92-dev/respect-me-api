import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.bmuschko.docker-remote-api") version "9.3.1"
}

version = "0.2.5"

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
//    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.112.Final")
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    runtimeOnly("com.h2database:h2:2.3.232")
//    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // B3 Propagation 구현 라이브러리
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.flywaydb:flyway-core:9.16.3")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.google.firebase:firebase-admin:9.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.1.214")
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
    inputDir.set(file(".")) // Dockerfile이 위치한 경로 (프로젝트 root 경로)
    images.add("elensar92/respect-me-member-api:${version}-test")
    group = "docker"
}

tasks.register<DockerPushImage>("pushTestImage") {
    dependsOn("buildTestImage")
    images.add("elensar92/respect-me-member-api:${version}-test")
    group = "docker"
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.member-api")
    archiveVersion.set("latest")
}

tasks {
    bootRun {
        jvmArgs = listOf("-Dspring.profiles.active=local")
    }
}

tasks.test {
    useJUnitPlatform()
}

