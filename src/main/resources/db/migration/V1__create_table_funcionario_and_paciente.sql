CREATE SEQUENCE IF NOT EXISTS funcionarios_seq
    START WITH 1
    INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS pacientes_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS funcionarios (
    id BIGINT NOT NULL DEFAULT nextval('funcionarios_seq'),
    cpf VARCHAR(255),
    nome VARCHAR(255),
    senha VARCHAR(255),
    roles VARCHAR(255)[],
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS pacientes (
    altura FLOAT NOT NULL,
    peso FLOAT NOT NULL,
    dt_nascimento TIMESTAMP(6),
    id BIGINT NOT NULL DEFAULT nextval('pacientes_seq'),
    cpf VARCHAR(255) UNIQUE,
    nome VARCHAR(255),
    uf VARCHAR(255),
    PRIMARY KEY (id)
);
