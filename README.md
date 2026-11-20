
# Sistema de Gestão Hoteleira

Sistema de gestão hoteleira desenvolvido em **Java Swing** com **MySQL**, sem frameworks externos. Permite gerir reservas, check-in, check-out, consumos, pagamentos, faturação, histórico de quartos e relatórios financeiros.

---

## Índice

- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Requisitos](#requisitos)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Base de dados](#base-de-dados)
- [Como executar](#como-executar)
- [Credenciais de teste](#credenciais-de-teste)
- [Fluxo de utilização](#fluxo-de-utilização)
- [Regras de negócio](#regras-de-negócio)
- [Autor](#autor)

---

## Funcionalidades

### Operacionais
- Login por funcionário com três níveis de permissão: `ADMIN`, `RECEPCAO`, `LIMPEZA`
- Dashboard com contagens em tempo real (quartos disponíveis, reservados, ocupados, chegadas e saídas do dia)
- Gestão de quartos (criar, listar e alterar estado)
- Reservas com pesquisa de cliente por documento e calendário para datas
- Check-in a partir de lista de reservas pendentes
- Consumos por quarto ocupado (restaurante, lavandaria, transporte, eventos)
- Check-out bloqueado até o pagamento estar completo
- Gestão de funcionários e clientes

### Financeiras
- Registo de pagamentos com múltiplos métodos: `DINHEIRO`, `CARTAO`, `TRANSFERENCIA`, `MPESA`
- Estados de pagamento: `PENDENTE`, `PARCIAL`, `PAGO`
- Cálculo automático de fatura (alojamento + consumos - desconto)
- Desconto de 10% para estadias superiores a 7 dias
- Fatura visual e exportação para ficheiro `.txt` na pasta `faturas/`
- Relatório financeiro com totais mensais, número de pagamentos, ticket médio e gráfico de barras dos últimos 6 meses

### Auditoria
- Trigger na base de dados que regista automaticamente todas as mudanças de estado dos quartos na tabela `HistoricoQuarto`
- Ecrã para consultar o histórico por quarto ou global

---

## Arquitetura

O projeto segue o padrão **MVC com camadas**, respeitando o princípio da responsabilidade única.

```
View  →  Controller  →  Service  →  Repository  →  Base de Dados
```

| Camada | Responsabilidade |
|--------|------------------|
| `Model` | Entidades do domínio (POJOs). Não contêm SQL nem lógica de interface. |
| `Repository` | Acesso à base de dados. Todo o SQL está aqui. Transações geridas com `setAutoCommit` e `commit`/`rollback`. |
| `Service` | Regras de negócio. Validações e cálculos. É aqui que vive a lógica do hotel. |
| `Controller` | Orquestra chamadas entre View e Service. Sem SQL e sem lógica de negócio. |
| `View` | Interface Swing. Só desenha janelas e captura eventos. |
| `Util` | Utilitários (ligação à BD, escrita de fatura, conversão de datas). |

---

## Requisitos

- **Java JDK 17** ou superior
- **XAMPP** (com MySQL 8) ou **MySQL Server 8**
- **mysql-connector-j-8.x.x.jar** (incluído na pasta `lib/`)
- **jcalendar-1.4.jar** (incluído na pasta `lib/` — para o calendário nas datas)
- **IntelliJ IDEA** (recomendado) ou Eclipse / NetBeans

---

## Estrutura do projeto

```
Sistema_Gestao_Hotel/
├── src/
│   ├── Controller/         # Controladores MVC
│   ├── Model/              # Entidades
│   │   └── Enums/          # Enumerações (EstadoQuarto, EstadoReserva, EstadoPagamento, MetodoPagamento)
│   ├── Repository/         # DAOs (acesso à BD)
│   ├── Service/            # Regras de negócio
│   ├── Util/               # Utilitários
│   ├── View/               # Ecrãs Swing
│   ├── Main.java           # Ponto de entrada
│   └── db.properties       # Configuração da ligação à BD
├── lib/                    # Bibliotecas externas (JARs)
├── faturas/                # Faturas geradas (criada em tempo de execução)
└── README.md
```

---

## Base de dados

### 1. Criar a base de dados

```sql
CREATE DATABASE IF NOT EXISTS hotelaria
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE hotelaria;
```

### 2. Criar as tabelas

```sql
-- Pessoa (superclasse)
CREATE TABLE IF NOT EXISTS pessoa (
    IdPessoa INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    contacto VARCHAR(20),
    endereco VARCHAR(150),
    Sexo VARCHAR(20),
    documentoIdentificacao VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Cliente (herda de pessoa)
CREATE TABLE IF NOT EXISTS Cliente (
    IdPessoa INT PRIMARY KEY,
    Nacionalidade VARCHAR(60),
    CONSTRAINT fk_cliente_pessoa
        FOREIGN KEY (IdPessoa) REFERENCES pessoa(IdPessoa)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Funcionário (herda de pessoa)
CREATE TABLE IF NOT EXISTS Funcionario (
    IdPessoa INT PRIMARY KEY,
    cargo VARCHAR(60),
    horarioTrabalho INT,
    nivelPermissao VARCHAR(30),
    salario DOUBLE,
    CONSTRAINT fk_funcionario_pessoa
        FOREIGN KEY (IdPessoa) REFERENCES pessoa(IdPessoa)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Quarto
CREATE TABLE IF NOT EXISTS quarto (
    numeroQuarto INT PRIMARY KEY,
    tipoQuarto VARCHAR(50),
    precoDiario DOUBLE,
    EstadoQuarto VARCHAR(20) DEFAULT 'DISPONIVEL'
) ENGINE=InnoDB;

-- Reserva
CREATE TABLE IF NOT EXISTS reserva (
    idReserva INT AUTO_INCREMENT PRIMARY KEY,
    dataReserva DATE NOT NULL,
    CheckIn DATE NOT NULL,
    CheckOut DATE NOT NULL,
    EstadoReserva VARCHAR(20) DEFAULT 'PENDENTE',
    numeroQuarto INT,
    idCliente INT,
    CONSTRAINT fk_reserva_quarto
        FOREIGN KEY (numeroQuarto) REFERENCES quarto(numeroQuarto),
    CONSTRAINT fk_reserva_cliente
        FOREIGN KEY (idCliente) REFERENCES Cliente(IdPessoa)
) ENGINE=InnoDB;

-- Serviço (superclasse)
CREATE TABLE IF NOT EXISTS Servico (
    IdServico INT AUTO_INCREMENT PRIMARY KEY,
    PrecoBase DOUBLE NOT NULL
) ENGINE=InnoDB;

-- Restaurante (herda de Servico)
CREATE TABLE IF NOT EXISTS Restaurante (
    IdServico INT PRIMARY KEY,
    TipoRefeicao VARCHAR(50),
    CONSTRAINT fk_restaurante_servico
        FOREIGN KEY (IdServico) REFERENCES Servico(IdServico)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Lavandaria (herda de Servico)
CREATE TABLE IF NOT EXISTS Lavandaria (
    IdServico INT PRIMARY KEY,
    quantPecas INT,
    CONSTRAINT fk_lavandaria_servico
        FOREIGN KEY (IdServico) REFERENCES Servico(IdServico)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Transporte (herda de Servico)
CREATE TABLE IF NOT EXISTS Transporte (
    IdServico INT PRIMARY KEY,
    distanciakm DOUBLE,
    CONSTRAINT fk_transporte_servico
        FOREIGN KEY (IdServico) REFERENCES Servico(IdServico)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Eventos (herda de Servico)
CREATE TABLE IF NOT EXISTS Eventos (
    IdServico INT PRIMARY KEY,
    numeroDias INT,
    CONSTRAINT fk_eventos_servico
        FOREIGN KEY (IdServico) REFERENCES Servico(IdServico)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Hospedagem
CREATE TABLE IF NOT EXISTS Hospedagem (
    idHospedagem INT AUTO_INCREMENT PRIMARY KEY,
    dataCheckIn DATE NOT NULL,
    dataCheckOut DATE,
    idReserva INT NOT NULL,
    idCliente INT NOT NULL,
    CONSTRAINT fk_hospedagem_reserva
        FOREIGN KEY (idReserva) REFERENCES reserva(idReserva),
    CONSTRAINT fk_hospedagem_cliente
        FOREIGN KEY (idCliente) REFERENCES Cliente(IdPessoa)
) ENGINE=InnoDB;

-- Ligação hospedagem-serviço (consumos)
CREATE TABLE IF NOT EXISTS hospedagem_servico (
    idHospedagem INT NOT NULL,
    idServico INT NOT NULL,
    PRIMARY KEY (idHospedagem, idServico),
    CONSTRAINT fk_hs_hospedagem
        FOREIGN KEY (idHospedagem) REFERENCES Hospedagem(idHospedagem)
        ON DELETE CASCADE,
    CONSTRAINT fk_hs_servico
        FOREIGN KEY (idServico) REFERENCES Servico(IdServico)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Pagamento
CREATE TABLE IF NOT EXISTS Pagamento (
    idPagamento INT AUTO_INCREMENT PRIMARY KEY,
    idHospedagem INT NOT NULL,
    dataPagamento DATE NOT NULL,
    valor DOUBLE NOT NULL,
    metodo VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PAGO',
    CONSTRAINT fk_pagamento_hospedagem
        FOREIGN KEY (idHospedagem) REFERENCES Hospedagem(idHospedagem)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- Histórico de quarto
CREATE TABLE IF NOT EXISTS HistoricoQuarto (
    idHistorico INT AUTO_INCREMENT PRIMARY KEY,
    numeroQuarto INT NOT NULL,
    estadoAnterior VARCHAR(20),
    estadoNovo VARCHAR(20) NOT NULL,
    dataAlteracao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idReserva INT,
    observacao VARCHAR(150),
    CONSTRAINT fk_historico_quarto
        FOREIGN KEY (numeroQuarto) REFERENCES quarto(numeroQuarto)
        ON DELETE CASCADE
) ENGINE=InnoDB;
```

### 3. Criar o trigger de histórico

```sql
DROP TRIGGER IF EXISTS trg_historico_quarto;

DELIMITER $$

CREATE TRIGGER trg_historico_quarto
AFTER UPDATE ON quarto
FOR EACH ROW
BEGIN
    IF OLD.EstadoQuarto <> NEW.EstadoQuarto THEN
        INSERT INTO HistoricoQuarto (numeroQuarto, estadoAnterior, estadoNovo, dataAlteracao, observacao)
        VALUES (NEW.numeroQuarto, OLD.EstadoQuarto, NEW.EstadoQuarto, NOW(), 'Alteracao automatica');
    END IF;
END$$

DELIMITER ;
```

### 4. Inserir dados iniciais

```sql
-- Quartos
INSERT INTO quarto (numeroQuarto, tipoQuarto, precoDiario, EstadoQuarto) VALUES
(101, 'Simples', 5000.0, 'DISPONIVEL'),
(102, 'Duplo', 8500.0, 'DISPONIVEL'),
(103, 'Duplo', 8500.0, 'DISPONIVEL'),
(104, 'Simples', 5000.0, 'DISPONIVEL'),
(201, 'Suite Premium', 15000.0, 'DISPONIVEL'),
(202, 'Suite Premium', 15000.0, 'DISPONIVEL'),
(203, 'Suite Junior', 11000.0, 'DISPONIVEL'),
(204, 'Duplo', 8500.0, 'DISPONIVEL'),
(205, 'Suite Premium', 15000.0, 'DISPONIVEL'),
(301, 'Suite Presidencial', 25000.0, 'DISPONIVEL'),
(302, 'Suite Presidencial', 25000.0, 'DISPONIVEL'),
(303, 'Suite Junior', 11000.0, 'DISPONIVEL');

-- Funcionário Admin
INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao)
VALUES ('Admin', '840000000', 'Sede', 'M', 'ADMIN001');

INSERT INTO Funcionario (IdPessoa, cargo, horarioTrabalho, nivelPermissao, salario)
VALUES (
    (SELECT IdPessoa FROM pessoa WHERE documentoIdentificacao = 'ADMIN001'),
    'Gestor', 8, 'ADMIN', 30000.0
);

-- Funcionário Recepção
INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao)
VALUES ('Maria Recepcao', '841111111', 'Recepcao', 'F', 'RECEP001');

INSERT INTO Funcionario (IdPessoa, cargo, horarioTrabalho, nivelPermissao, salario)
VALUES (
    (SELECT IdPessoa FROM pessoa WHERE documentoIdentificacao = 'RECEP001'),
    'Recepcionista', 8, 'RECEPCAO', 18000.0
);

-- Funcionário Limpeza
INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao)
VALUES ('Pedro Limpeza', '846666666', 'Bairro X, 10', 'M', 'LIMPEZA001');

INSERT INTO Funcionario (IdPessoa, cargo, horarioTrabalho, nivelPermissao, salario)
VALUES (
    (SELECT IdPessoa FROM pessoa WHERE documentoIdentificacao = 'LIMPEZA001'),
    'Empregado de Limpeza', 8, 'LIMPEZA', 12000.0
);

-- Cliente de teste
INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao)
VALUES ('Joao Silva', '842222222', 'Av. Eduardo Mondlane, 123', 'M', '110100123456A');

INSERT INTO Cliente (IdPessoa, Nacionalidade)
VALUES (
    (SELECT IdPessoa FROM pessoa WHERE documentoIdentificacao = '110100123456A'),
    'Mocambicana'
);
```

### 5. Comandos úteis de gestão

**Ver todas as tabelas:**
```sql
SHOW TABLES FROM hotelaria;
```

**Ver estado dos quartos:**
```sql
SELECT numeroQuarto, tipoQuarto, EstadoQuarto FROM quarto ORDER BY numeroQuarto;
```

**Ver reservas e estados:**
```sql
SELECT r.idReserva, r.CheckIn, r.CheckOut, r.EstadoReserva,
       p.nome AS cliente, r.numeroQuarto
FROM reserva r
INNER JOIN pessoa p ON r.idCliente = p.IdPessoa
ORDER BY r.idReserva;
```

**Ver hospedagens ativas:**
```sql
SELECT h.idHospedagem, h.dataCheckIn, h.dataCheckOut,
       p.nome AS cliente, q.numeroQuarto
FROM Hospedagem h
INNER JOIN pessoa p ON h.idCliente = p.IdPessoa
INNER JOIN reserva r ON h.idReserva = r.idReserva
INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto
WHERE h.dataCheckOut IS NULL;
```

**Ver consumos por hospedagem:**
```sql
SELECT hs.idHospedagem, s.IdServico, s.PrecoBase,
       r.TipoRefeicao, l.quantPecas, t.distanciakm, e.numeroDias
FROM hospedagem_servico hs
INNER JOIN Servico s ON hs.idServico = s.IdServico
LEFT JOIN Restaurante r ON s.IdServico = r.IdServico
LEFT JOIN Lavandaria l ON s.IdServico = l.IdServico
LEFT JOIN Transporte t ON s.IdServico = t.IdServico
LEFT JOIN Eventos e ON s.IdServico = e.IdServico;
```

**Ver pagamentos:**
```sql
SELECT p.idPagamento, p.idHospedagem, p.dataPagamento, p.valor, p.metodo, p.estado,
       pe.nome AS cliente
FROM Pagamento p
INNER JOIN Hospedagem h ON p.idHospedagem = h.idHospedagem
INNER JOIN pessoa pe ON h.idCliente = pe.IdPessoa
ORDER BY p.dataPagamento DESC;
```

**Ver histórico de um quarto:**
```sql
SELECT idHistorico, numeroQuarto, estadoAnterior, estadoNovo, dataAlteracao
FROM HistoricoQuarto
WHERE numeroQuarto = 101
ORDER BY dataAlteracao DESC;
```

**Total faturado no mês atual:**
```sql
SELECT COALESCE(SUM(valor), 0) AS total_mes
FROM Pagamento
WHERE MONTH(dataPagamento) = MONTH(CURDATE())
  AND YEAR(dataPagamento) = YEAR(CURDATE());
```

**Reset de dados (apaga tudo, exceto quartos):**
```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE hospedagem_servico;
TRUNCATE TABLE Pagamento;
TRUNCATE TABLE Hospedagem;
TRUNCATE TABLE reserva;
TRUNCATE TABLE Cliente;
TRUNCATE TABLE Funcionario;
TRUNCATE TABLE Restaurante;
TRUNCATE TABLE Lavandaria;
TRUNCATE TABLE Transporte;
TRUNCATE TABLE Eventos;
TRUNCATE TABLE Servico;
TRUNCATE TABLE pessoa;
TRUNCATE TABLE HistoricoQuarto;
SET FOREIGN_KEY_CHECKS = 1;
```

---

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/GotaGames/Gestao_Hoteleira.git
```

### 2. Abrir no IntelliJ IDEA

1. `File` → `Open` → selecionar a pasta do projeto
2. Confirmar que `src` está marcada como **Sources Root** (botão direito em `src` → `Mark Directory as` → `Sources Root`)

### 3. Adicionar as bibliotecas ao projeto

1. `File` → `Project Structure` → `Modules` → `Dependencies`
2. `+` → `JARs or directories`
3. Selecionar `lib/mysql-connector-j-8.x.x.jar`
4. Repetir para `lib/jcalendar-1.4.jar`
5. `Apply` → `OK`

### 4. Configurar a ligação à base de dados

Editar o ficheiro `src/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/hotelaria?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=
db.driver=com.mysql.cj.jdbc.Driver
```

Se o MySQL tiver password, preencher `db.password`.

### 5. Correr o projeto

Abrir `src/Main.java` e executar (`Shift+F10` no IntelliJ).

---

## Credenciais de teste

| Nível | Documento | Permissão | Acesso |
|-------|-----------|-----------|--------|
| ADMIN | `ADMIN001` | `ADMIN` | Acesso total |
| RECEPCAO | `RECEP001` | `RECEPCAO` | Sem gestão de funcionários nem relatório financeiro |
| LIMPEZA | `LIMPEZA001` | `LIMPEZA` | Apenas consulta operacional |

---

## Fluxo de utilização

### Fluxo completo de uma estadia

1. **Funcionário faz login** com documento e nível de permissão
2. **Nova reserva** → pesquisar cliente por documento (ou criar novo) → escolher quarto disponível → escolher datas no calendário → confirmar
    - O quarto passa a `RESERVADO`
3. **Check-in** → selecionar a reserva na lista de pendentes → confirmar
    - O quarto passa a `OCUPADO`
4. **Adicionar consumo** (opcional) → escolher quarto ocupado → escolher tipo de serviço → indicar preço e detalhes
5. **Pagamento** → escolher quarto ocupado → registar pagamento (parcial ou total)
6. **Check-out** → escolher quarto ocupado → escolher data
    - Se o saldo estiver zerado, a fatura aparece e o quarto volta a `DISPONIVEL`
    - Se houver saldo em falta, o check-out é bloqueado

### Fluxo de consulta

- **Clientes** → lista completa com pesquisa por nome ou documento
- **Histórico de Quartos** → escolher quarto e ver todas as mudanças de estado
- **Relatório Financeiro** → totais mensais, ticket médio e gráfico dos últimos 6 meses

---

## Regras de negócio

| Regra | Onde está implementada |
|-------|------------------------|
| Um quarto não pode ser reservado se não estiver `DISPONIVEL` | `ReservaService.criar` |
| A data de check-out deve ser posterior à de check-in | `ReservaService.criar`, `FaturaService.calcularDiasHospedagem` |
| O check-out exige pagamento total | `CheckOutService.realizarCheckOut` |
| Desconto de 10% para estadias superiores a 7 dias | `FaturaService.calcularDesconto` |
| O documento de identificação é único por pessoa | Restrição `UNIQUE` na tabela `pessoa` |
| Cada mudança de estado de quarto é registada | Trigger `trg_historico_quarto` na BD |
| O cliente é criado automaticamente se não existir na reserva | `ReservaFrame.reservar` |

---

## Autor

Projeto desenvolvido no âmbito da disciplina de Programação Orientada a Objetos.

- **Nomes:** Gota, Denzel; Cossa, Alberto; Mithe, Cleyton; Uanheta, Wilson; Vilanculos, Widson.
- **Instituição:** Universidade Eduardo Mondlane, Departamento de Eletrotecnia, Licenciatura em Engenharia Informatica - Pos-laboral, 2 Ano.
- **Ano:** 2026

---

## Licença

Projeto académico de uso educacional.



