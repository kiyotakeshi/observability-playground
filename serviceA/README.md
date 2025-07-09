# ServiceA

## Prepare for opentelemetry-javaagent.jar

```sh
curl -O --location https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.16.0/opentelemetry-javaagent.jar
```

## Run

```sh
./gradlew bootRun
```

## Jeager

http://localhost:16686/search