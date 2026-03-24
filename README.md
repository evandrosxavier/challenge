# MyDelivery API - Sistema de Gestão de Restaurantes 🍔🚀

Bem-vindo ao MyDelivery API, um sistema robusto de gestão de entregas desenvolvido como parte do Tech Challenge (Fase 2) do curso de Arquitetura e Desenvolvimento Java da FIAP.

Esta API foi projetada para gerenciar usuários, tipos de usuários, restaurantes e itens de cardápio, utilizando as melhores práticas de desenvolvimento moderno e arquitetura de software.

## 🏗️ Arquitetura do Projeto

O projeto utiliza a Arquitetura Hexagonal (Ports & Adapters) combinada com princípios de Clean Architecture, garantindo que o núcleo da aplicação seja independente de tecnologias externas.

### Estrutura de Pacotes:

- **application**: Contém a lógica de negócio (service), as interfaces de saída (port) e o tratamento de erros customizado (exception).
- **domain**: Contém as entidades de negócio puras (entities).
- **infrastructure**: Implementa os adaptadores de persistência (persistence/adapter) e configurações técnicas (config).
- **interfaces**: Camada de entrada com controllers, dtos e mappers (MapStruct).
- **security**: Implementação de segurança baseada em JWT (JSON Web Token).

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Data JPA (Persistência)
- PostgreSQL (Banco de Dados Relacional)
- Docker & Docker Compose (Containerização)
- MapStruct (Mapeamento de DTOs)
- Jakarta Bean Validation (Validação de Dados)
- Spring Security & JWT (Autenticação e Autorização)
- OpenAPI / Swagger UI (Documentação da API)
- JUnit 5 & Mockito (Testes Automatizados)

## 🚀 Como Executar a Aplicação

A aplicação está totalmente containerizada, facilitando o setup do ambiente.

### Pré-requisitos:

- Docker e Docker Compose instalados.

### Passo a Passo:

1. Clone o repositório:

```bash
git clone https://github.com/evandrosxavier/challenge.git
cd challenge
```

2. Suba os containers (API + Banco de Dados):

```bash
docker-compose up --build
```

3. A API estará disponível em: http://localhost:8080

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, você pode acessar a documentação interativa do Swagger para testar os endpoints:

🔗 **Swagger UI**: http://localhost:8080/swagger-ui.html

## 📌 Principais Endpoints da API

| Recurso | Método | Endpoint | Descrição |
|---------|--------|----------|-----------|
| Autenticação | POST | /auth/login | Autentica usuário e gera token JWT |
| Usuários | POST | /api/v1/usuarios | Cadastro de novos usuários |
| Usuários | GET | /api/v1/usuarios | Listagem e filtros de usuários |
| Tipos de Usuário | POST | /api/v1/tipo-usuario | Cadastro de tipos (CLIENTE, DONO, etc) |
| Restaurantes | POST | /api/v1/restaurantes | Cadastro de novos estabelecimentos |
| Restaurantes | GET | /api/v1/restaurantes | Listagem de todos os restaurantes |
| Itens de Cardápio | POST | /api/v1/itens-cardapio | Cadastro de itens para um restaurante |
| Itens de Cardápio | GET | /api/v1/itens-cardapio | Listagem de itens e filtros |

## 🔄 Fluxo de Utilização (Ordem de Cadastro)

Para garantir a integridade dos dados e o correto funcionamento dos relacionamentos, siga a ordem de cadastro abaixo ao testar a API:

1. **Tipo de Usuário**: Cadastre primeiro os tipos (ex: CLIENTE, DONO_RESTAURANTE).
2. **Usuário**: Cadastre o usuário vinculando-o a um tipoUsuarioId existente.
3. **Restaurante**: Cadastre o estabelecimento vinculando-o a um usuarioId (Dono) existente.
4. **Item de Cardápio**: Cadastre os itens vinculando-os a um restauranteId existente.

## ✅ Qualidade e Testes

O projeto possui um forte compromisso com a qualidade do código, apresentando:

- **Cobertura de Testes**: Superior a 90%.
- **Tratamento de Erros**: Padronizado via BusinessException e ErrorCode (RFC 7807).
- **SOLID & Clean Code**: Aplicados em todas as camadas do sistema.

Para rodar os testes localmente:

```bash
./mvnw test
```

## 📂 Collections para Teste

Na raiz do projeto, você encontrará a collection do Postman para facilitar os testes manuais:

- `postman/mydelivery_collection.json`

## 👥 Autor

- **Evandro Santos Xavier** - RM: 368088
- **Curso**: Arquitetura e Desenvolvimento Java - FIAP
- **GitHub**: evandrosxavier
- **Repositório**: challenge

## 📄 Licença

Este projeto é para fins acadêmicos como parte do Tech Challenge da FIAP.

