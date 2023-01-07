INSERT INTO UNDERLIER_CATEGORY (name) VALUES ('中证500品种');

INSERT INTO UNDERLIER (code, category_id) VALUES ('000905.SH', 1);
INSERT INTO UNDERLIER (code, category_id) VALUES ('IC2212.CFE', 1);

INSERT INTO OTC_OPTION_CONTRACT (contract_num, underlier_id, option_product, notional, effective_date,
maturity_date, initial_price, exercise_price, participate_ratio, status) VALUES
('OTC202301070001', 1, '非保本雪球', 10000000.00, '2023-01-09', '2024-01-09', 6233.62, 6233.62, 1.00, 1);

INSERT INTO HEDGE_POSITION (hedge_num, underlier_id, net_position, market_value, daily_pnl, delta) VALUES
('HDG202301070001', 2, 2198, 2190616600.00, 47564720.00, 1234567.89);

INSERT INTO OTC_OPTION_PORTFOLIO (name) VALUES ('投资组合A');

INSERT INTO PORTFOLIO_CONTRACT (portfolio_id, contract_num) VALUES (1, 'OTC202301070001');