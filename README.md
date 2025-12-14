# Criação e manutenção de usuário

Este repositório contém a entrega da Atividade Avaliativa focada na implementação das funcionalidades de controle e manutenção de usuário.

Relatório de Requisitos não implementados: https://docs.google.com/document/d/1IcbJpu5d1V8u3sJHeyb0YyOGnuab9fgAPyVCM_BVxh0/edit?usp=sharing

## 1. Integrantes do Grupo

| Nome Completo
| :--- 
| Erik Satlher
| Ikaro James Xavier da Neiva
| Igor Wendling Gurgel Silva

---

## 2. Estrutura do Projeto

O sistema segue o padrão de design **Model-View-Presenter (MVP)**,

### Estrutura de Pastas:

* `model/`: Contém as classes de dados da aplicação
* `view/`: Contém as interfaces de View (Contratos)
* `presenter/`: Contém a lógica de controle e gerencia o ciclo de vida das Views.
* `service/`: Contém as regras de negócio (RNs) e orquestra as operações.
* `repository/`: Gerencia a persistência dos dados (Implementações JDBC para SQLite).

### Implementação de Requisitos Externos

O projeto integra módulos externos de infraestrutura:

.  **Validação de Senha:** Implementado via dependência externa Maven (`validadorsenha`).

---

## 3. Instruções de Build e Execução

Para construir e executar o projeto, os seguintes pré-requisitos são necessários:

### 3.1 Dependências e Pré-requisitos

| Item | Versão Específica | Função |
| :--- | :--- | :--- |
| **Linguagem/Runtime** | Java Development Kit (JDK) **17** | Ambiente de execução necessário. |
| **Ferramenta de Build** | Apache Maven | Gerenciador de dependências e build. |
| **Banco de Dados** | SQLite | Usado para persistência local do sistema (`atividade.db`). |
| **SQLite Driver** | `org.xerial:sqlite-jdbc` **3.42.0.0** | Dependência JDBC para conexão com o banco. |
| **Validador Senha** | `com.github.claytonfraga:validadorsenha` | Dependência externa para validação. |

### 3.2 Comandos de Build

Este projeto utiliza **Java 17** e **Maven**.

1. Clone o repositório:
   ```bash
   git clone https://github.com/igorwgsilva/UserControl.git

    2. Acesse a pasta:
Bash
cd UserControl
3. Compile e execute via Maven (ou abra no NetBeans):

    mvn clean install

    mvn exec:java -Dexec.mainClass="view.JanelaPrincipalView"
