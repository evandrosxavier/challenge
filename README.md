MyDelivery API - Sistema de Gestão de Entregas

A MyDelivery API é uma solução robusta de backend desenvolvida para o gerenciamento de usuários e endereços, servindo como o módulo fundamental para um ecossistema completo de gestão de entregas. Este projeto foi concebido com foco em escalabilidade, segurança e aderência às melhores práticas de desenvolvimento de software moderno.

O sistema implementa uma API RESTful completa, utilizando o ecossistema Spring Boot 3 e Java 21, com persistência em banco de dados relacional e orquestração via containers, garantindo um ambiente de execução consistente e isolado.




🏗️ Arquitetura do Sistema

A aplicação adota a Arquitetura em Camadas (Layered Architecture), um padrão que promove a separação de preocupações e facilita a manutenção e evolução do código.

| Camada | Responsabilidade |
| --- | --- |
| **Controller** | Camada de apresentação que expõe os endpoints REST e gerencia o contrato da API via DTOs. |
| **Service** | Camada de lógica de negócio, onde residem as validações e orquestração de processos. |
| **Repository** | Camada de persistência que abstrai o acesso ao banco de dados utilizando Spring Data JPA. |
| **Model** | Representação das entidades de domínio e mapeamento objeto-relacional (ORM). |




A infraestrutura é composta por um banco de dados PostgreSQL 16, orquestrado através do Docker Compose, o que simplifica o setup inicial e garante a paridade entre os ambientes de desenvolvimento e produção.




🚀 Tecnologias e Boas Práticas

O projeto foi desenvolvido sob o rigor de padrões de qualidade da indústria, garantindo um código limpo e profissional.

•Spring Boot 3 & Java 21: Utilização das versões mais recentes para aproveitar melhorias de performance e sintaxe.

•SOLID & DRY: Aplicação dos princípios de design para um código coeso e sem repetições desnecessárias.

•Problem Detail (RFC 7807): Padronização das respostas de erro, fornecendo mensagens claras e estruturadas para os consumidores da API.

•Bean Validation: Validação rigorosa dos dados de entrada diretamente nos DTOs.

•OpenAPI 3 (Swagger): Documentação interativa e autodocumentada para facilitar o consumo da API.




🛠️ Configuração e Execução

A aplicação está totalmente containerizada, o que elimina a necessidade de instalações manuais de banco de dados ou dependências locais.

Pré-requisitos

•Docker Desktop instalado e em execução.

•Git para clonagem do repositório.

Passo a Passo

1.Clonar o Repositório:

Bash
git clone https://github.com/evandrosxavier/challenge
cd challenge


2.Executar com Docker Compose:

Bash
docker-compose up --build

O comando --build garante que a imagem da aplicação seja construída com as alterações mais recentes.


3.Acessar a Documentação:
Após a inicialização, a documentação interativa estará disponível em:
http://localhost:8080/swagger-ui.html




📑 Endpoints da API

A API está organizada para oferecer uma experiência intuitiva e padronizada.


Autenticação

•POST /auth/login: Autentica o usuário e retorna um token JWT.

Gerenciamento de Usuários (/api/v1/usuarios)

•POST /: Criação de novos usuários com endereços vinculados.

•GET /: Listagem geral ou filtrada por nome.

•GET /{id}: Busca detalhada por identificador único.

•PUT /{id}: Atualização de dados cadastrais.

•PATCH /{id}/senha: Atualização segura de credenciais.

•DELETE /{id}: Remoção de registros do sistema.



🧪 Testes e Validação

Para validar o funcionamento da API, você pode utilizar a Collection do Postman oficial do projeto:

🔗 Acessar Collection Postman

https://www.postman.com/mar8c8os/portiflio-fiap-postech/collection/11931430-29dba819-9187-4b2a-a8d7-ed09883406b3/?action=share&creator=11931430

Além disso, o Swagger UI local permite realizar testes manuais diretamente pelo navegador, validando cenários de sucesso e as respostas padronizadas de erro.




👤 Autor

Evandro Santos Xavier

•GitHub: evandrosxavier

•Projeto: Challenge FIAP - MyDelivery API

