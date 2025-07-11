# ビルドステージ
FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

# Gradle Wrapper とビルドファイルをコピー
COPY gradlew ./
COPY gradle/ ./gradle/
COPY build.gradle.kts settings.gradle.kts ./

# 依存関係のダウンロード（レイヤーキャッシュ最適化）
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# ソースコードをコピー
COPY src/ ./src/

# アプリケーションをビルド
RUN ./gradlew build -x test --no-daemon

# 実行ステージ
FROM openjdk:21-jdk-slim

WORKDIR /app

# 必要なパッケージをインストール（curl でヘルスチェック用）
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# ビルドステージから JAR ファイルをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

# OpenTelemetry エージェントは外部からマウントする想定（起動時に外部から渡すためコメントアウト）
# COPY ./opentelemetry-javaagent.jar ./

# 実行ユーザーを作成（セキュリティ向上）
RUN groupadd -r appuser && useradd -r -g appuser -m appuser
RUN chown -R appuser:appuser /app
USER appuser

# ヘルスチェック
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8081

# JVM 最適化オプションを追加
CMD ["java", \
     "-Xmx512m", \
     "-Xms256m", \
     "-XX:+UseG1GC", \
     "-XX:+UseContainerSupport", \
     "-javaagent:opentelemetry-javaagent.jar", \
     "-jar", "app.jar"]