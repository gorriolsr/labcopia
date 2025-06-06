DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id           VARCHAR(36) PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    deleted      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    user_type    VARCHAR(20)  NOT NULL CHECK (user_type IN ('Outlaw', 'Sheriff')),
    bounty       INT                   DEFAULT 0,
    status       VARCHAR(50),
    salary       INT                   DEFAULT 0,
    captures     INT                   DEFAULT 0,
    eliminations INT                   DEFAULT 0
);