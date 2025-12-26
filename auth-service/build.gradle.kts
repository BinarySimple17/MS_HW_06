plugins {
	java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
//	id("org.springframework.boot") version "3.5.9"
//	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.binarysimple"
version = "0.0.1-SNAPSHOT"
description = "Auth Service"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
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

    implementation(libs.jsonwebtoken.api)
    runtimeOnly(libs.jsonwebtoken.impl)
    runtimeOnly(libs.jsonwebtoken.jackson)

//    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
//    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
//    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
//	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
//	compileOnly("org.projectlombok:lombok")
//	annotationProcessor("org.projectlombok:lombok")
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("io.projectreactor:reactor-test")
//	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

//dependencyManagement {
//	imports {
//		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//	}
//}

tasks.withType<Test> {
	useJUnitPlatform()
}
