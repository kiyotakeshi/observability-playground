INSERT INTO employees (name, email, department, position) VALUES
('田中 太郎', 'tanaka@example.com', 'Engineering', 'Senior Developer'),
('佐藤 花子', 'sato@example.com', 'Marketing', 'Marketing Manager'),
('山田 次郎', 'yamada@example.com', 'Sales', 'Sales Representative'),
('鈴木 美咲', 'suzuki@example.com', 'HR', 'HR Specialist'),
('高橋 健一', 'takahashi@example.com', 'Finance', 'Financial Analyst')
ON CONFLICT DO NOTHING;