
CREATE TABLE UNDERLIER_CATEGORY (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(10) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE UNDERLIER (
    id INTEGER NOT NULL AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL,
    category_id INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT FK_UNDERLIER_CATEGORY FOREIGN KEY (category_id) REFERENCES UNDERLIER_CATEGORY(id)
);

CREATE TABLE OTC_OPTION_CONTRACT (
    contract_num VARCHAR(15), -- OTC<YYYYmmdd><fourdigits>
    underlier_id INTEGER,
    option_product VARCHAR(20),
    notional DECIMAL(20,2),
    effective_date DATE,
    maturity_date DATE,
    initial_price DECIMAL(10,2),
    exercise_price DECIMAL(10,2),
    participate_ratio DECIMAL(4,2),
    status CHAR(1), -- 0 了结, 1 存续
    PRIMARY KEY(contract_num),
    CONSTRAINT FK_OTC_UNDERLIER FOREIGN KEY (underlier_id) REFERENCES UNDERLIER(id)
);

CREATE TABLE HEDGE_POSITION (
    hedge_num VARCHAR(15), -- HDG<YYYYmmdd><fourdigits>
    underlier_id INTEGER NOT NULL,
    net_position INTEGER,
    market_value DECIMAL(20,2),
    daily_pnl DECIMAL(20,2),
    delta DECIMAL(20,2),
    PRIMARY KEY(hedge_num),
    CONSTRAINT FK_HDG_UNDERLIER FOREIGN KEY (underlier_id) REFERENCES UNDERLIER(id)
);

CREATE TABLE OTC_OPTION_PORTFOLIO (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE PORTFOLIO_CONTRACT (
    portfolio_id INTEGER,
    contract_num VARCHAR(15),
    PRIMARY KEY (portfolio_id, contract_num),
    CONSTRAINT FK_JOIN_PORTFOLIO FOREIGN KEY (portfolio_id) REFERENCES OTC_OPTION_PORTFOLIO(id),
    CONSTRAINT FK_JOIN_CONTRACT FOREIGN KEY (contract_num) REFERENCES OTC_OPTION_CONTRACT(contract_num)
);