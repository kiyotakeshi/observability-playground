plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "observability.com.example"
version = "0.0.1-SNAPSHOT"

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
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.16.0")
	implementation("io.opentelemetry:opentelemetry-extension-kotlin:1.49.0")

	runtimeOnly("org.postgresql:postgresql")

//	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	jvmArgs = listOf(
		// 複数のサービスで共通で opentelemetry-javaagent.jar を使用するため一つ上のディレクトリに配置する
		"-javaagent:${project.rootDir.parent}/opentelemetry-javaagent.jar",
		"-Dotel.service.name=serviceB",
		"-Dotel.exporter.otlp.endpoint=http://localhost:4317",
		"-Dotel.exporter.otlp.protocol=grpc",
		"-Dotel.metrics.exporter=none",
		"-Dotel.logs.exporter=none",
	)
}
