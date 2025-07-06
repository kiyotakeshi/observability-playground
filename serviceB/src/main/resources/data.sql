INSERT INTO companies (company_name, industry, company_size, address, phone, website, created_at, updated_at) VALUES
('株式会社テクノロジー', 'IT・ソフトウェア', '中小企業', 'サンプル県サンプル市サンプル町1-1-1', '03-1234-5678', 'https://technology.example.com', NOW(), NOW()),
('グローバル商事株式会社', '商社・卸売', '大企業', 'テスト県テスト市テスト区2-2-2', '03-2345-6789', 'https://global-trade.example.com', NOW(), NOW()),
('イノベーション製造株式会社', '製造業', '中小企業', 'ダミー県ダミー市ダミー区3-3-3', '06-3456-7890', 'https://innovation-mfg.example.com', NOW(), NOW()),
('ヘルスケア・メディカル株式会社', '医療・ヘルスケア', '中小企業', 'モック県モック市モック区4-4-4', '045-4567-8901', 'https://healthcare-medical.example.com', NOW(), NOW()),
('フィナンシャル・サービス株式会社', '金融・保険', '大企業', 'フェイク県フェイク市フェイク区5-5-5', '03-5678-9012', 'https://financial-service.example.com', NOW(), NOW())
ON CONFLICT DO NOTHING;