import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

version = "0.0.1"

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
    api("io.netty:netty-resolver-dns-native-macos:4.1.105.Final:osx-aarch_64")
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("com.auth0:java-jwt:4.4.0")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.aspectj:aspectjrt:1.9.20")
    api("com.google.code.gson:gson:2.8.8")
    api("org.springframework:spring-tx")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = false
}

tasks.withType<org.gradle.api.tasks.bundling.Jar> {
    archiveBaseName.set("${rootProject.group}.common")
    archiveVersion.set("${version}")
    archiveClassifier.set("lib")
}

tasks.test {
    useJUnitPlatform()
}