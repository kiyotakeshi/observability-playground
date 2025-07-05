# ServiceB API リクエスト例

## 会社一覧取得
```bash
curl -s http://localhost:8081/companies | jq .
```

## 会社詳細取得
```bash
curl -s http://localhost:8081/companies/1 | jq .
```

## 会社統計情報取得
```bash
curl -s http://localhost:8081/companies/stats | jq .
```

## キャッシュクリア
```bash
curl -s -X DELETE http://localhost:8081/companies/cache | jq .
```

## Redis キャッシュの CLI での確認方法

### Redis コンテナに接続
```bash
docker exec -it serviceB-redis redis-cli
```

### キャッシュされたキーを確認
```bash
# 全てのキーを表示
KEYS *

# 特定のパターンのキーを表示
KEYS company:*
KEYS companies*
```

### キャッシュの内容を確認
```bash
# 会社統計情報のキャッシュ内容を確認
HGETALL company:stats

# 会社一覧のキャッシュ内容を確認
GET companies::SimpleKey []

# 特定会社のキャッシュ内容を確認
GET company::1
```

### キャッシュの有効期限を確認
```bash
# TTL（Time To Live）を確認
TTL company:stats
```

### キャッシュを手動削除
```bash
# 統計情報キャッシュを削除
DEL company:stats

# Spring Boot のキャッシュを削除
DEL companies::SimpleKey []
DEL company::1
```