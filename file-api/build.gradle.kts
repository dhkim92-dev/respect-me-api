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

version = "0.0.1"

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":common"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("org.postgresql:postgresql:42.5.4")
//    implementation("org.flywaydb:flyway-core:9.16.3")
//    implementation("io.micrometer:micrometer-tracing-bridge-brave")
//    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
//    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
//    implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")

//    testRuntimeOnly("com.h2database:h2:2.1.214")
//    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
//    kapt("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}