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

version = "0.0.3"

repositories {
    mavenCentral()
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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kapt {
    correctErrorTypes = true
}

tasks.register<DockerBuildImage>("buildTestImage") {
    buildArgs.put("SPRING_PROFILES_ACTIVE", "test")
    dependsOn("bootJar")
    inputDir.set(file(".")) // Dockerfile이 위치한 경로 (프로젝트 root 경로)
    images.add("elensar92/respect-me-auth-api:${version}-test")
    group = "docker"
}

tasks.register<DockerPushImage>("pushTestImage") {
    dependsOn("buildTestImage")
    images.add("elensar92/respect-me-auth-api:${version}-test")
    group = "docker"
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.auth-api")
    archiveVersion.set("latest")
}

tasks.test {
    useJUnitPlatform()
}