
plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
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
    implementation(project(":common"))
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core:4.4.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kapt {
    correctErrorTypes = true
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.auth-api")
    archiveVersion.set("${version}")
}

tasks.test {
    useJUnitPlatform()
}