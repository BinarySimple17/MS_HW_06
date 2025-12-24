plugins {
	java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
//	id("org.springframework.boot") version "3.5.9"
//	id("io.spring.dependency-management") version "1.1.7"
//	id("java-library")
//	id("io.freefair.lombok") version "8.12"
}

group = "ru.binarysimple"
version = "0.0.1-SNAPSHOT"
description = "Auth service"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.liquibase.core)
    implementation(libs.jakarta.validation.api)
    implementation(libs.spring.boot.actuator)
    implementation(libs.postgresql)
    implementation(libs.springdoc.openapi.ui)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.spring.boot.starter.test)
    compileOnly("org.mapstruct:mapstruct:1.6.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.micrometer:micrometer-registry-prometheus")
//	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.projectlombok:lombok")
//	implementation("org.mapstruct:mapstruct:1.6.0")
//	runtimeOnly("org.postgresql:postgresql")
//	annotationProcessor("org.projectlombok:lombok")
//	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.security:spring-security-test")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//	testAnnotationProcessor("org.projectlombok:lombok")
//	testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
