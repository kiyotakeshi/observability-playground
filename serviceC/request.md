# API 動作確認手順

## GraphiQL を使用した確認

以下のクエリを GraphiQL(http://localhost:8082/graphiql?path=/graphql) で実行して API の動作を確認できます。

### 全ての Deal を取得

```graphql
query MyQuery {
  deals {
    amount
    companyId
    createdAt
    description
    employeeId
    id
    status
    title
    updatedAt
  }
}
```

### 特定の Deal を ID で取得

```graphql
query MyQuery {
  deal(id: "1") {
    id
    companyId
    description
  }
}
```
