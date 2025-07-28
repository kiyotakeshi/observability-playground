serviceA が request する時点での header には特に付与はない
javaagent がヘッダーを付与するタイミングは RestTemplate や Apollo client のリクエストを intercept してログを仕込んだタイミングよりも後のよう。

```
2025-07-28T13:19:09.150+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.s.client.CompanyServiceClient      : serviceB に company の存在チェックを開始: companyId=1
2025-07-28T13:19:47.330+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] HTTP Request Headers:
2025-07-28T13:20:10.810+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Request Header: Accept = application/json, application/*+json
2025-07-28T13:20:10.810+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Request Header: Content-Length = 0

2025-07-28T13:20:10.820+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] HTTP Response Headers:
2025-07-28T13:20:10.820+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Response Header: Content-Type = application/json
2025-07-28T13:20:10.820+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Response Header: Transfer-Encoding = chunked
2025-07-28T13:20:10.820+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Response Header: Date = Mon, 28 Jul 2025 04:20:10 GMT
2025-07-28T13:20:10.820+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Response Header: Keep-Alive = timeout=60
2025-07-28T13:20:10.821+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.config.RestClientConfig   : [serviceB] Response Header: Connection = keep-alive
2025-07-28T13:20:10.823+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.s.client.CompanyServiceClient      : serviceB から company を取得成功: companyId=1, companyName=株式会社テクノロジー

2025-07-28T13:20:10.824+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.client.DealServiceClient  : serviceC に deal の作成を開始: employeeId=1, companyId=1, title=新規システム開発案件
2025-07-28T13:20:29.561+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] GraphQL Request Headers:
2025-07-28T13:20:49.299+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Request Header: X-APOLLO-OPERATION-ID = 1bfff43582e35dd2a8a9f7aa2350b96aab3144043681a11717b714d447039832
2025-07-28T13:20:49.299+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Request Header: X-APOLLO-OPERATION-NAME = CreateDeal
2025-07-28T13:20:49.299+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Request Header: Accept = multipart/mixed; deferSpec=20220824, application/json
2025-07-28T13:20:49.299+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Request Header: User-Agent = serviceA-client/1.0

2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] GraphQL Response Headers:
2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Response Header: Content-Type = application/json
2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Response Header: Transfer-Encoding = chunked
2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Response Header: Date = Mon, 28 Jul 2025 04:20:49 GMT
2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Response Header: Keep-Alive = timeout=60
2025-07-28T13:20:49.340+09:00  INFO 80312 --- [serviceA] [atcher-worker-1] o.c.e.s.config.GraphQLClientConfig       : [serviceC] Response Header: Connection = keep-alive
2025-07-28T13:20:49.349+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.serviceA.client.DealServiceClient  : serviceC で deal の作成が成功: dealId=6, employeeId=1, companyId=1
2025-07-28T13:20:49.349+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.s.s.DealOrchestrationService       : deal 作成プロセスが正常に完了: dealId=6, employeeId=1, companyId=1
2025-07-28T13:20:49.349+09:00  INFO 80312 --- [serviceA] [nio-8080-exec-1] o.c.e.s.controller.EmployeeController    : deal 作成が正常に完了: dealId=6
```

serviceB が受信した header

```
2025-07-28T13:36:46.171+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] 受信したリクエストヘッダー:
2025-07-28T13:36:46.172+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] Header: accept = application/json, application/*+json
2025-07-28T13:36:46.172+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] Header: traceparent = 00-3fb8cc0dbd3f15198bb404252ce8de18-9961056938d7011a-01
2025-07-28T13:36:46.172+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] Header: user-agent = Java/21.0.4
2025-07-28T13:36:46.172+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] Header: host = localhost:8081
2025-07-28T13:36:46.172+09:00  INFO 87304 --- [serviceB] [nio-8081-exec-1] o.c.e.s.controller.CompanyController     : [serviceB] Header: connection = keep-alive
```

serviceC が受信した header

```
2025-07-28T13:36:46.626+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] 受信したリクエストヘッダー:
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: x-apollo-operation-id = 1bfff43582e35dd2a8a9f7aa2350b96aab3144043681a11717b714d447039832
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: x-apollo-operation-name = CreateDeal
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: accept = multipart/mixed; deferSpec=20220824, application/json
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: user-agent = serviceA-client/1.0
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: content-type = application/json
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: content-length = 429
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: host = localhost:8082
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: connection = Keep-Alive
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: accept-encoding = gzip
2025-07-28T13:37:16.161+09:00  INFO 86326 --- [serviceC] [nio-8082-exec-1] o.c.e.s.controller.DealController        : [serviceC] Header: traceparent = 00-3fb8cc0dbd3f15198bb404252ce8de18-1aa275c7c7ad5df6-01
```

Claude Code にみてもらったところ、traceId と spanId が関係していた。
[jeager で export した json](./jeager-log-3fb8cc0dbd3f15198bb404252ce8de18.json)でも trace と span の関係が同じように表現されていた！

**重要な発見:**

- **Trace ID が同一**: `3fb8cc0dbd3f15198bb404252ce8de18` - 両サービスで同じ
- **Span ID が異なる**: 各サービス呼び出しで個別の span として追跡
  - serviceB: `9961056938d7011a`
  - serviceC: `1aa275c7c7ad5df6`
