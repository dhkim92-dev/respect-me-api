import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

version = "0.0.2"

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

//    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.8.1")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")
    kaptTest("org.mapstruct:mapstruct-processor:1.5.2.Final")
    implementation("com.h2database:h2:2.1.214")
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

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    archiveBaseName.set("${rootProject.group}.member-api")
    archiveVersion.set("${version}")
}

tasks.test {
    useJUnitPlatform()
}