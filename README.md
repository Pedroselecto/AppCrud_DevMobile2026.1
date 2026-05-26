## 📱 Sobre o Projeto

Este repositório contém duas frentes principais:
* **`apiCrud` (Back-end):** Uma API REST desenvolvida com Spring Boot para simular um sistema de gerenciamento de produtos de uma loja.
* **`appCrud` (Front-end Mobile):** Aplicativo Android feito com Kotlin.

## 🚀 Funcionalidades

O aplicativo realiza todas as operações básicas de um sistema (CRUD):
- **C**reate: Cadastro de novos produtos com código, nome e preço.
- **R**ead: Listagem em tempo real de todos os produtos do banco.
- **U**pdate: Atualização dos dados de produtos existentes.
- **D**elete: Exclusão de itens diretamente da base de dados.

## 🛠️ Tecnologias e Arquitetura

### Back-end (`apiCrud`)
* **Java / Spring Boot:** Criação dos endpoints REST (`@GetMapping`, `@PostMapping`, etc.).
* **MySQL:** Banco de dados relacional para armazenamento dos dados.

### Front-end Mobile (`appCrud`)
* **Kotlin:** Linguagem principal de desenvolvimento.
* **StateFlow & Coroutines:** Gerenciamento de estados e execução de chamadas em background sem travar a interface do usuário.
* **Retrofit & Gson:** Cliente HTTP moderno para realizar as requisições à API e converter as respostas JSON em objetos Kotlin.

--

## Foto do protótipo
<img width="383" height="797" alt="print-prototipo" src="https://github.com/user-attachments/assets/4da62897-bb10-42d7-abcb-b3407e5292ff" />

A interface é atualizada constantemente mostrando a lista de produtos e seus respectivos dados;
Preencher as informações e selecionar a opção "Salvar no Banco" adiciona o item no banco;
O botão de deletar remove o item do banco. 
