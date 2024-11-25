# Projeto Tasy

Este projeto é uma aplicação construída com Java 17, Spring Boot, Spring Security e PostgreSQL.

## Requisitos

- [Java 17](https://adoptopenjdk.net/)
- [Docker](https://www.docker.com/)
- [PostgreSQL](https://www.postgresql.org/) caso prefira rodar o banco de dados localmente.

## Estrutura do Projeto

O sistema possui uma estrutura simples, composta por duas entidades principais: Funcionários e Pacientes. Além disso, há um Identity Provider responsável por autenticação e autorização de algumas rotas.

## Configuração de Ambiente

### Subir o Banco de Dados

Utilize o seguinte comando para inicializar o banco via Docker:
```text
docker compose up -d
```

### Gerar as Chaves

Caso prefira não usar as chaves já disponíveis no repositório, você pode gerar novas com:
```text
make key
```

### Compilar e Subir a Aplicação

Para compilar e executar a aplicação, utilize:

```text
./mvnw spring-boot:run -DskipTests
```

**Nota: As migrations para criação das tabelas serão aplicadas automaticamente.**

# Fluxo de Login e Regras de Acesso para Pacientes
## 1. Criaçao de funcionário
**Endpoint:**  `POST /funcionario`

### **Exemplo de Requisição com Role de MEDICO:**
```json
{
  "cpf":"75700646153",
  "senha":"123456",
  "nome": "DR.Perfeito",
  "role": "MEDICO"
}
```

### **Exemplo de Requisição com Role de ENFERMEIRO:**
```json
{
  "cpf":"795.460.350-41",
  "senha":"123456",
  "nome": "Sara Flor",
  "role": "ENFERMEIRO"
}
```

## 2. Realizar Login (_para login utilize um funcionário já criado_)
**Endpoint:**  `POST /login` 

### **Exemplo de Requisição:**
```json
{
  "cpf":"75700646153",
  "senha": "123456"
}
```

### **Resposta de Sucesso:**
```json
{
  "token": "jwt_token_gerado",
  "expiresIn": 300
}
```
O token JWT recebido deve ser utilizado como Bearer Token no cabeçalho das requisições subsequentes.

---

## 2. Operações no Domínio Paciente e Regras de Acesso

Os endpoints do domínio `Paciente` possuem restrições baseadas no **Role** do usuário autenticado.

### **Regras de Acesso:**
| **Operação**       | **Endpoint**            | **Roles Permitidos** |
|---------------------|-------------------------|-----------------------|
| Criar Paciente      | `POST /paciente`       | **MEDICO**, **ENFERMEIRO** |
| Atualizar Paciente  | `PUT /paciente/{id}`   | **MEDICO**            |
| Deletar Paciente    | `DELETE /paciente/{id}`| **MEDICO**            |
| Consultar Paciente  | `GET /paciente/{id}`   | **MEDICO**            |

---

## 3. Exemplo de Uso do Token JWT

### **Cabeçalho para Requisições Autenticadas:**
```http
Authorization: Bearer jwt_token_gerado
```

### **Criar Paciente**
**Endpoint:** `POST /paciente`

**Corpo da Requisição:**
```json
{
  "nome": "João Silva",
  "cpf": "360.237.020-82",
  "dtNascimento": "1990-05-15",
  "peso": 75.5,
  "altura": 1.80,
  "uf": "SP"
}
```
---

### Atualizar um Paciente

**Endpoint:** `PUT /paciente/{id}
`

Exemplo de corpo da requisição:

```json
{
"nome": "João Silva",
"cpf": "31962114104",
"dtNascimento": "1990-05-15",
"peso": 75.5,
"altura": 1.80,
"uf": "SP"
}
```

---

### Deletar um Paciente

**Endpoint:**  `DELETE /paciente/{id}
`
---

### Consultar um Paciente

**Endpoint:** `/paciente/{id}`

Exemplo de Respose: 
```json
{
"nome": "João Silva",
"cpf": "31962114104",
"dtNascimento": "1990-05-15",
"peso": 75.5,
"altura": 1.80,
"uf": "SP"
}
```