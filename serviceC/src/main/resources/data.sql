INSERT INTO deals (title, description, employee_id, company_id, amount, status, created_at, updated_at) VALUES
('新規システム開発案件', 'クラウド基盤を利用した新規システムの開発案件', 1, 1, 5000000, '進行中', NOW() - INTERVAL '10 days', NOW() - INTERVAL '1 day'),
('既存システム改修案件', '既存のレガシーシステムのモダナイゼーション案件', 2, 2, 3000000, '提案中', NOW() - INTERVAL '5 days', NOW()),
('データ分析基盤構築', 'ビッグデータ分析のための基盤構築案件', 1, 3, 8000000, '完了', NOW() - INTERVAL '30 days', NOW() - INTERVAL '15 days')
ON CONFLICT DO NOTHING;