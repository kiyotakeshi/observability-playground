# observerbility-playground

## Prepare for opentelemetry-javaagent.jar

```sh
curl -O --location https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.16.0/opentelemetry-javaagent.jar
```

## run

serviceA, B, C runs as container with related middleware(Postgres, Redis)

```sh
mise run start
```

## run service individually

- [serviceA README](./serviceA/README.md)
- [serviceB README](./serviceB/README.md)
- [serviceC README](./serviceC/README.md)
