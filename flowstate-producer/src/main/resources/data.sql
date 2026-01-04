INSERT INTO accounts (id, user_id, balance, currency, version)
VALUES ('acc-1', 'user-1', 1000.00, 'RUB', 1)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO accounts (id, user_id, balance, currency, version)
VALUES ('acc-2', 'user-2', 500.00, 'RUB', 1)
    ON CONFLICT (id) DO NOTHING;