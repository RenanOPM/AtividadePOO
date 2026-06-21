-- ============================================================
--  SISTEMA DE PRODUÇÃO DE CACHAÇA
--  Banco de Dados: PostgreSQL
--  Versão: 1.0
-- ============================================================

-- ============================================================
--  REMOÇÃO DAS TABELAS (ordem inversa das dependências)
-- ============================================================
DROP TABLE IF EXISTS item_producao      CASCADE;
DROP TABLE IF EXISTS producao           CASCADE;
DROP TABLE IF EXISTS armazenamento      CASCADE;
DROP TABLE IF EXISTS cachaca            CASCADE;
DROP TABLE IF EXISTS local_armazenamento CASCADE;
DROP TABLE IF EXISTS tipo_cachaca       CASCADE;
DROP TABLE IF EXISTS linha_producao     CASCADE;
DROP TABLE IF EXISTS funcionario        CASCADE;
DROP TABLE IF EXISTS gerente            CASCADE;

-- ============================================================
--  1. GERENTE
-- ============================================================
CREATE TABLE gerente (
    id_gerente   SERIAL        PRIMARY KEY,
    nome         VARCHAR(100)  NOT NULL,
    cpf          CHAR(11)      NOT NULL UNIQUE,
    email        VARCHAR(150)  NOT NULL,
    telefone     VARCHAR(15),
    data_admissao DATE         NOT NULL DEFAULT CURRENT_DATE,
    ativo        BOOLEAN       NOT NULL DEFAULT TRUE
);

INSERT INTO gerente (nome, cpf, email, telefone, data_admissao) VALUES
    ('João Silva',       '12345678901', 'joao@cachacaria.com',   '27999990001', '2020-01-10'),
    ('Maria Fernandes',  '98765432100', 'maria@cachacaria.com',  '27999990002', '2021-03-15');

-- ============================================================
--  2. FUNCIONÁRIO
-- ============================================================
CREATE TABLE funcionario (
    id_funcionario  SERIAL        PRIMARY KEY,
    nome            VARCHAR(100)  NOT NULL,
    cpf             CHAR(11)      NOT NULL UNIQUE,
    email           VARCHAR(150)  NOT NULL,
    telefone        VARCHAR(15),
    cargo           VARCHAR(80)   NOT NULL,
    data_admissao   DATE          NOT NULL DEFAULT CURRENT_DATE,
    ativo           BOOLEAN       NOT NULL DEFAULT TRUE,
    id_gerente      INTEGER       NOT NULL REFERENCES gerente(id_gerente)
);

INSERT INTO funcionario (nome, cpf, email, telefone, cargo, data_admissao, id_gerente) VALUES
    ('Renan Costa',      '11122233344', 'renan@cachacaria.com',  '27988880001', 'Operador de Produção',  '2021-06-01', 1),
    ('Lucas Pereira',    '55566677788', 'lucas@cachacaria.com',  '27988880002', 'Auxiliar de Produção',  '2022-02-20', 1),
    -- Funcionário próximo do limite (para teste de regra de negócio)
    ('Carlos Souza',     '99988877766', 'carlos@cachacaria.com', '27988880003', 'Operador de Produção',  '2022-08-10', 2),
    ('Ana Rodrigues',    '44455566677', 'ana@cachacaria.com',    '27988880004', 'Auxiliar de Armazenamento','2023-01-05', 2);

-- ============================================================
--  3. TIPO DE CACHAÇA
-- ============================================================
CREATE TABLE tipo_cachaca (
    id_tipo         SERIAL        PRIMARY KEY,
    nome            VARCHAR(100)  NOT NULL UNIQUE,
    descricao       VARCHAR(255),
    embalagem       VARCHAR(80)   NOT NULL,  -- ex: Pacote de Plástico, Tampinha, Rolha
    volume_litros   NUMERIC(5,3)  NOT NULL,  -- volume unitário em litros
    tempo_envelhecimento_dias INTEGER NOT NULL DEFAULT 0,  -- dias mínimos de envelhecimento
    ativo           BOOLEAN       NOT NULL DEFAULT TRUE
);

-- Tipos conforme especificação
INSERT INTO tipo_cachaca (nome, descricao, embalagem, volume_litros, tempo_envelhecimento_dias) VALUES
    ('Barrigudinho',  'Barril de cachaça em pacote plástico',     'Pacote de Plástico', 0.500,  90),
    ('Tampinha',      'Litro de cachaça com tampa de plástico',   'Tampinha',           1.000,  30),
    ('Rolha',         'Litro de cachaça com rolha de cortiça',    'Rolha',              1.000, 180),
    ('Meiota',        'Cachaça em garrafa meia-garrafa',          'Tampinha',           0.500,  60),
    ('Mula Preta',    'Cachaça especial envelhecida Mula Preta',  'Rolha',              1.000, 365);

-- ============================================================
--  4. LINHA DE PRODUÇÃO  (exclusiva por tipo — RN)
-- ============================================================
CREATE TABLE linha_producao (
    id_linha        SERIAL        PRIMARY KEY,
    descricao       VARCHAR(150)  NOT NULL,
    capacidade_dia  NUMERIC(8,2)  NOT NULL,  -- capacidade máxima em litros/dia
    ativa           BOOLEAN       NOT NULL DEFAULT TRUE,
    id_tipo         INTEGER       NOT NULL UNIQUE  -- 1 linha por tipo (UNIQUE garante exclusividade)
                    REFERENCES tipo_cachaca(id_tipo)
);

INSERT INTO linha_producao (descricao, capacidade_dia, id_tipo) VALUES
    ('Linha Barrigudinho',  500.00, 1),
    ('Linha Tampinha',      800.00, 2),
    ('Linha Rolha',         600.00, 3),
    ('Linha Meiota',        400.00, 4),
    ('Linha Mula Preta',    300.00, 5);

-- ============================================================
--  5. LOCAL DE ARMAZENAMENTO
-- ============================================================
CREATE TABLE local_armazenamento (
    id_local        SERIAL        PRIMARY KEY,
    nome            VARCHAR(100)  NOT NULL UNIQUE,
    descricao       VARCHAR(255),
    capacidade_litros NUMERIC(10,2) NOT NULL,
    ativo           BOOLEAN       NOT NULL DEFAULT TRUE
);

INSERT INTO local_armazenamento (nome, descricao, capacidade_litros) VALUES
    ('Galpão A',      'Galpão principal de armazenamento',          5000.00),
    ('Galpão B',      'Galpão secundário de armazenamento',         3000.00),
    ('Câmara Fria 1', 'Câmara de temperatura controlada 1',         1000.00),
    ('Câmara Fria 2', 'Câmara de temperatura controlada 2',         1000.00),
    ('Depósito Externo','Depósito externo para excedente',          2000.00);

-- ============================================================
--  6. PRODUÇÃO  (cabeçalho)
-- ============================================================
CREATE TABLE producao (
    id_producao     SERIAL        PRIMARY KEY,
    data_producao   DATE          NOT NULL DEFAULT CURRENT_DATE,
    observacao      TEXT,
    total_litros    NUMERIC(10,2) NOT NULL DEFAULT 0,
    id_gerente      INTEGER       NOT NULL REFERENCES gerente(id_gerente),
    id_linha        INTEGER       NOT NULL REFERENCES linha_producao(id_linha),
    id_local        INTEGER       NOT NULL REFERENCES local_armazenamento(id_local),
    criado_em       TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- ============================================================
--  7. ITEM DE PRODUÇÃO  (detalhe — funcionário x lote)
-- ============================================================
CREATE TABLE item_producao (
    id_item         SERIAL        PRIMARY KEY,
    quantidade_litros NUMERIC(8,2) NOT NULL CHECK (quantidade_litros > 0),
    qtd_engradados  INTEGER       NOT NULL CHECK (qtd_engradados > 0),
    litros_por_engradado NUMERIC(6,2) NOT NULL, -- todos os engradados com mesma qtd (RN)
    selagem_verificada   BOOLEAN  NOT NULL DEFAULT FALSE, -- checagem de selagem (RN)
    id_producao     INTEGER       NOT NULL REFERENCES producao(id_producao),
    id_funcionario  INTEGER       NOT NULL REFERENCES funcionario(id_funcionario),
    CONSTRAINT ck_engradado CHECK (
        quantidade_litros = qtd_engradados * litros_por_engradado
    )
);

-- ============================================================
--  8. CACHAÇA  (lote armazenado / envelhecimento)
-- ============================================================
CREATE TABLE cachaca (
    id_cachaca          SERIAL        PRIMARY KEY,
    lote                VARCHAR(50)   NOT NULL UNIQUE,
    data_producao       DATE          NOT NULL,
    data_liberacao      DATE          NOT NULL,  -- calculada via tempo_envelhecimento (RN)
    quantidade_litros   NUMERIC(10,2) NOT NULL CHECK (quantidade_litros > 0),
    status              VARCHAR(30)   NOT NULL DEFAULT 'Em Envelhecimento'
                        CHECK (status IN ('Em Envelhecimento','Liberado','Embalo','Vendido')),
    observacao          TEXT,
    id_tipo             INTEGER       NOT NULL REFERENCES tipo_cachaca(id_tipo),
    id_local            INTEGER       NOT NULL REFERENCES local_armazenamento(id_local),
    id_producao         INTEGER       NOT NULL REFERENCES producao(id_producao)
);

-- ============================================================
--  DADOS DE TESTE — Produções e Itens
-- ============================================================

-- Produção 1: Barrigudinho — Renan (100 L) dentro do limite de 300L/dia
INSERT INTO producao (data_producao, total_litros, id_gerente, id_linha, id_local) VALUES
    ('2025-05-01', 100.00, 1, 1, 1);

INSERT INTO item_producao (quantidade_litros, qtd_engradados, litros_por_engradado, selagem_verificada, id_producao, id_funcionario) VALUES
    (100.00, 20, 5.00, TRUE, 1, 1);

-- Produção 2: Rolha — Lucas (200 L) — testar limite próximo de 300L
INSERT INTO producao (data_producao, total_litros, id_gerente, id_linha, id_local) VALUES
    ('2025-05-02', 200.00, 1, 3, 2);

INSERT INTO item_producao (quantidade_litros, qtd_engradados, litros_por_engradado, selagem_verificada, id_producao, id_funcionario) VALUES
    (200.00, 20, 10.00, TRUE, 2, 2);

-- Produção 3: Mula Preta — Carlos — 300 L (exatamente no limite, válido)
INSERT INTO producao (data_producao, total_litros, id_gerente, id_linha, id_local) VALUES
    ('2025-05-03', 300.00, 2, 5, 3);

INSERT INTO item_producao (quantidade_litros, qtd_engradados, litros_por_engradado, selagem_verificada, id_producao, id_funcionario) VALUES
    (300.00, 30, 10.00, TRUE, 3, 3);

-- Produção 4: Tampinha — Renan — para testar RN de 1500L/dia (produção parcial)
INSERT INTO producao (data_producao, total_litros, id_gerente, id_linha, id_local) VALUES
    ('2025-05-04', 250.00, 1, 2, 1);

INSERT INTO item_producao (quantidade_litros, qtd_engradados, litros_por_engradado, selagem_verificada, id_producao, id_funcionario) VALUES
    (250.00, 25, 10.00, TRUE, 4, 1);

-- ============================================================
--  DADOS DE TESTE — Cachaças armazenadas
-- ============================================================
INSERT INTO cachaca (lote, data_producao, data_liberacao, quantidade_litros, status, id_tipo, id_local, id_producao) VALUES
    ('LOTE-2025-001', '2025-05-01',
        '2025-05-01'::DATE + (SELECT tempo_envelhecimento_dias FROM tipo_cachaca WHERE id_tipo=1),
        100.00, 'Em Envelhecimento', 1, 1, 1),

    ('LOTE-2025-002', '2025-05-02',
        '2025-05-02'::DATE + (SELECT tempo_envelhecimento_dias FROM tipo_cachaca WHERE id_tipo=3),
        200.00, 'Em Envelhecimento', 3, 2, 2),

    ('LOTE-2025-003', '2025-05-03',
        '2025-05-03'::DATE + (SELECT tempo_envelhecimento_dias FROM tipo_cachaca WHERE id_tipo=5),
        300.00, 'Em Envelhecimento', 5, 3, 3),

    ('LOTE-2025-004', '2025-05-04',
        '2025-05-04'::DATE + (SELECT tempo_envelhecimento_dias FROM tipo_cachaca WHERE id_tipo=2),
        250.00, 'Liberado', 2, 1, 4);

-- ============================================================
--  VIEWS AUXILIARES
-- ============================================================

-- Total produzido por funcionário por dia (apoio à RN de 300 L)
CREATE OR REPLACE VIEW vw_producao_funcionario_dia AS
SELECT
    ip.id_funcionario,
    f.nome             AS funcionario,
    p.data_producao,
    SUM(ip.quantidade_litros) AS total_litros_dia
FROM item_producao ip
JOIN producao   p ON p.id_producao   = ip.id_producao
JOIN funcionario f ON f.id_funcionario = ip.id_funcionario
GROUP BY ip.id_funcionario, f.nome, p.data_producao;

-- Total produzido + armazenado por dia (apoio à RN de 1500 L)
CREATE OR REPLACE VIEW vw_total_dia AS
SELECT
    data_producao,
    SUM(total_litros) AS total_litros_dia
FROM producao
GROUP BY data_producao;

-- Quantidade de pacotes por mês e por tipo (gráfico)
CREATE OR REPLACE VIEW vw_pacotes_mes_tipo AS
SELECT
    DATE_TRUNC('month', p.data_producao) AS mes,
    tc.nome                              AS tipo_cachaca,
    tc.embalagem,
    SUM(ip.qtd_engradados)               AS total_pacotes,
    SUM(ip.quantidade_litros)            AS total_litros
FROM item_producao  ip
JOIN producao       p  ON p.id_producao = ip.id_producao
JOIN linha_producao lp ON lp.id_linha   = p.id_linha
JOIN tipo_cachaca   tc ON tc.id_tipo    = lp.id_tipo
GROUP BY DATE_TRUNC('month', p.data_producao), tc.nome, tc.embalagem
ORDER BY mes, tipo_cachaca;

-- Cachaças por local de armazenamento (ComboBox com múltipla escolha)
CREATE OR REPLACE VIEW vw_cachacas_por_local AS
SELECT
    la.nome          AS local_armazenamento,
    tc.nome          AS tipo_cachaca,
    c.lote,
    c.quantidade_litros,
    c.data_liberacao,
    c.status
FROM cachaca              c
JOIN local_armazenamento  la ON la.id_local = c.id_local
JOIN tipo_cachaca         tc ON tc.id_tipo  = c.id_tipo
ORDER BY la.nome, tc.nome;

-- ============================================================
--  FUNÇÕES / REGRAS DE NEGÓCIO (implementadas no banco)
-- ============================================================

-- RN1: Funcionário não pode produzir mais de 300 L/dia
CREATE OR REPLACE FUNCTION fn_verificar_limite_funcionario(
    p_id_funcionario INTEGER,
    p_data           DATE,
    p_litros         NUMERIC
) RETURNS BOOLEAN AS $$
DECLARE
    v_total NUMERIC;
BEGIN
    SELECT COALESCE(SUM(ip.quantidade_litros), 0)
    INTO v_total
    FROM item_producao ip
    JOIN producao p ON p.id_producao = ip.id_producao
    WHERE ip.id_funcionario = p_id_funcionario
      AND p.data_producao   = p_data;

    RETURN (v_total + p_litros) <= 300;
END;
$$ LANGUAGE plpgsql;

-- RN2: Produção + armazenamento não pode ultrapassar 1500 L/dia
CREATE OR REPLACE FUNCTION fn_verificar_limite_diario(
    p_data   DATE,
    p_litros NUMERIC
) RETURNS BOOLEAN AS $$
DECLARE
    v_total NUMERIC;
BEGIN
    SELECT COALESCE(SUM(total_litros), 0)
    INTO v_total
    FROM producao
    WHERE data_producao = p_data;

    RETURN (v_total + p_litros) <= 1500;
END;
$$ LANGUAGE plpgsql;

-- RN3: Calcular data de liberação automaticamente
CREATE OR REPLACE FUNCTION fn_calcular_data_liberacao(
    p_id_tipo       INTEGER,
    p_data_producao DATE
) RETURNS DATE AS $$
DECLARE
    v_dias INTEGER;
BEGIN
    SELECT tempo_envelhecimento_dias INTO v_dias
    FROM tipo_cachaca WHERE id_tipo = p_id_tipo;

    RETURN p_data_producao + v_dias;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
--  EXEMPLOS DE USO DAS FUNÇÕES
-- ============================================================

-- Verifica se Carlos pode produzir mais 50 L no dia 2025-05-03 (já tem 300 — deve retornar FALSE)
SELECT fn_verificar_limite_funcionario(3, '2025-05-03', 50) AS pode_produzir_carlos;

-- Verifica se é possível produzir mais 100 L no dia 2025-05-04 (tem 250, total 350 — deve retornar TRUE)
SELECT fn_verificar_limite_diario('2025-05-04', 100) AS pode_produzir_dia;

-- Calcula liberação de um Mula Preta produzido hoje
SELECT fn_calcular_data_liberacao(5, CURRENT_DATE) AS data_liberacao_mula_preta;

-- ============================================================
--  FIM DO SCRIPT
-- ============================================================
