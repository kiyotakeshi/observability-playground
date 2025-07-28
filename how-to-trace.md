# マイクロサービス間の分散トレーシング仕組み

## 概要

本プロジェクトでは Java Agent による自動計装を使用して、マイクロサービス間で分散トレーシングを実現しています。

## 分析結果

### traceparent ヘッダーの伝搬パターン

ログ分析から以下のことが判明しました：

**serviceB 受信ヘッダー:**

```
traceparent = 00-3fb8cc0dbd3f15198bb404252ce8de18-9961056938d7011a-01
```

**serviceC 受信ヘッダー:**

```
traceparent = 00-3fb8cc0dbd3f15198bb404252ce8de18-1aa275c7c7ad5df6-01
```

**重要な発見:**

- **Trace ID が同一**: `3fb8cc0dbd3f15198bb404252ce8de18` - 両サービスで同じ
- **Span ID が異なる**: 各サービス呼び出しで個別の span として追跡
  - serviceB: `9961056938d7011a`
  - serviceC: `1aa275c7c7ad5df6`

これは W3C Trace Context 仕様に準拠した正常な動作です。

### traceparent ヘッダー形式

`traceparent` ヘッダーは **W3C Trace Context 仕様** で定義された標準形式です（Java Agent 固有ではありません）。

**形式**: `version-trace_id-parent_id-trace_flags`

例: `00-3fb8cc0dbd3f15198bb404252ce8de18-9961056938d7011a-01`

- **`00`** (version): バージョン番号（現在は `00` 固定）
- **`3fb8cc0dbd3f15198bb404252ce8de18`** (trace_id): 128bit のトレース ID（32文字の16進数）
- **`9961056938d7011a`** (parent_id): 64bit の親スパン ID（16文字の16進数）
- **`01`** (trace_flags): トレースフラグ（最下位ビットが 1 = サンプリング有効）

[opentelemetry-java-instrumentation のテストでも上記の形式なのかテストしている](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/1115cda7a2f4aefa0bbc25aa64f9b35f694a2df4/testing-common/src/main/java/io/opentelemetry/instrumentation/testing/junit/http/AbstractHttpServerTest.java#L171-L177)

## Java Agent のヘッダー付与タイミング

### 処理順序

1. **Java Agent** が HTTP クライアント呼び出し時に `traceparent` ヘッダーを自動付与
2. **アプリケーションの Interceptor** が実行される（ログ出力タイミング）
3. **実際の HTTP 通信** が実行される

### 重要なポイント

- **serviceB への REST 呼び出し**: Java Agent がヘッダーを後から付与するため、送信側の Interceptor ログには `traceparent` が表示されない
- **serviceC への GraphQL 呼び出し**: Apollo Client が適切にヘッダーを伝搬し、受信側で確認可能

## 結論

マイクロサービス間で Trace が連携して確認できる理由は **Java Agent の自動計装** によるものです。

### 仕組み

- Java Agent が HTTP クライアント呼び出し時に自動的に `traceparent` ヘッダーを付与
- W3C Trace Context 仕様に従い、同一 Trace ID で各サービス間の通信を追跡
- 各サービス呼び出しは個別の Span として記録され、親子関係を保持

### なぜ送信側で見えないのか

- アプリケーションの Interceptor が実行されるタイミングより後に Java Agent がヘッダーを付与
- そのため送信側のログでは `traceparent` ヘッダーが見えないが、受信側では正常に確認できる

この仕組みにより、手動でのヘッダー管理なしに分散トレーシングが自動的に動作します。
