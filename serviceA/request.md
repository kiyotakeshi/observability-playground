# ServiceA API Requests

## Employee APIs

### 全ての employee を取得
```bash
curl -s http://localhost:8080/employees | jq .
```

### employee を ID で取得
```bash
curl -s http://localhost:8080/employees/1 | jq .
```

## Deal Creation API

### Deal 作成 (serviceB と serviceC と連携)
```bash
curl -X POST http://localhost:8080/employees/deals \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 1,
    "companyId": 1,
    "title": "新規システム開発案件",
    "description": "大手クライアントとの年間契約による新システム開発プロジェクト",
    "amount": 1500000.00,
    "status": "PENDING"
  }' -s | jq .
```

### 正常レスポンス例
```json
{
  "id": "1",
  "title": "新規システム開発案件",
  "description": "大手クライアントとの年間契約による新システム開発プロジェクト",
  "employeeId": 1,
  "companyId": 1,
  "amount": 1500000.00,
  "status": "PENDING",
  "createdAt": "2025-07-08T23:30:00",
  "updatedAt": "2025-07-08T23:30:00"
}
```

### エラーケース

#### 存在しない employee ID
```bash
curl -X POST http://localhost:8080/employees/deals \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 999,
    "companyId": 1,
    "title": "テスト案件",
    "description": "テスト",
    "amount": 100000.00,
    "status": "PENDING"
  }' -s | jq .
```

#### 存在しない company ID
```bash
curl -X POST http://localhost:8080/employees/deals \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 1,
    "companyId": 999,
    "title": "テスト案件",
    "description": "テスト",
    "amount": 100000.00,
    "status": "PENDING"
  }' -s | jq .
```

#### バリデーションエラー
```bash
curl -X POST http://localhost:8080/employees/deals \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": -1,
    "companyId": 1,
    "title": "",
    "description": "",
    "amount": -100.00,
    "status": ""
  }' -s | jq .
```

## API 動作確認手順

1. **serviceA, serviceB, serviceC が全て起動していることを確認**
   ```bash
   # serviceA (port 8080)
   curl -s http://localhost:8080/employees
   
   # serviceB (port 8081)
   curl -s http://localhost:8081/companies
   
   # serviceC (port 8082)
   curl -s http://localhost:8082/graphql -H "Content-Type: application/json" -d '{"query": "{ deals { id title } }"}'
   ```

2. **正常な deal 作成をテスト**
   - 上記の deal 作成 API を実行
   - serviceA → serviceB → serviceC の順で通信が行われる
   - 各ステップでログが出力される

3. **エラーケースをテスト**
   - 存在しない employee ID / company ID でリクエスト
   - バリデーションエラーのリクエスト
   - 適切なエラーメッセージが返却されることを確認