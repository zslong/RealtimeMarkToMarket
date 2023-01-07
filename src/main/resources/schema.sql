
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
    contract_num VARCHAR(15), -- <otc/hdg><YYYYmmdd><fourdigits>
    underlier_id INTEGER,
    option_product VARCHAR(20),
    notional DECIMAL(20,2),
    effective_date DATE,
    maturity_date DATE,
    initial_price DECIMAL(10,2),
    participate_ratio DECIMAL(1,2),
    status CHAR(1), -- 0 了结, 1 存续
    PRIMARY KEY(contract_num),
    CONSTRAINT FK_UNDERLIER FOREIGN KEY (underlier_id) REFERENCES UNDERLIER(id)
);