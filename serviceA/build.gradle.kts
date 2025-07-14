plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	id("com.apollographql.apollo3") version "3.8.4"
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.apollographql.apollo3:apollo-runtime:3.8.4")
	implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.16.0")
	implementation("io.opentelemetry:opentelemetry-extension-kotlin:1.49.0")

	runtimeOnly("org.postgresql:postgresql")

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

apollo {
	service("servicec") {
		packageName.set("observability.com.example.serviceA.graphql")
		schemaFile.set(file("src/main/graphql/schema.graphqls"))
		srcDir("src/main/graphql")
		mapScalar("Long", "kotlin.Long")
	}
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	jvmArgs = listOf(
		// 複数のサービスで共通で opentelemetry-javaagent.jar を使用するため一つ上のディレクトリに配置する
		"-javaagent:${project.rootDir.parent}/opentelemetry-javaagent.jar",
		"-Dotel.service.name=serviceA",
		"-Dotel.exporter.otlp.endpoint=http://localhost:4317",
		"-Dotel.exporter.otlp.protocol=grpc",
		"-Dotel.metrics.exporter=otlp",
		"-Dotel.logs.exporter=otlp",
	)
}
