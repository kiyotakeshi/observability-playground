# ServiceA API リクエスト例

## 従業員一覧取得

```bash
curl -s http://localhost:8080/employees | jq .
```

## 従業員一件取得

```bash
curl -s http://localhost:8080/employees/1 | jq .
```
