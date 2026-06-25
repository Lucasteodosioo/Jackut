# Jackut — Projeto (Lógica de Negócio)

Sistema de rede social desenvolvido como projeto da disciplina de Programação II (P2). Implementa toda a lógica de negócio de gerenciamento de usuários, comunidades, relacionamentos e mensagens. Os testes de aceitação são executados com **EasyAccept** a partir dos arquivos em `tests/`.

## Execução

Execute pelo IntelliJ (botão **Run**)

## Estrutura do Projeto

```
Jackut/
├── src/br/ufal/ic/p2/jackut/     Código fonte
│   ├── Facade.java                Interface pública dos testes
│   ├── SistemaJackut.java         Lógica principal
│   ├── Usuario.java               Entidade de usuário
│   └── Comunidade.java            Entidade de comunidade
├── tests/                          Testes de aceitação (EasyAccept)
├── lib/easyaccept*.jar            Biblioteca EasyAccept
├── usuarios.dat                    Arquivo de persistência (gerado)
└── README.md                       Este arquivo
```

## Armazenamento de Dados

Os dados são persistidos em um arquivo binário serializado (`usuarios.dat`) localizado na raiz do projeto:

### Como Funciona
- **Formato**: Serialização Java via `ObjectOutputStream` e `ObjectInputStream`
- **Estrutura**: Mapa contendo dois objetos principais:
  - `usuarios`: `Map<String, Usuario>` — Todos os usuários cadastrados com seus dados e relacionamentos
  - `comunidades`: `Map<String, Comunidade>` — Todas as comunidades criadas
  
### Ciclo de Persistência
1. Ao iniciar o sistema, `SistemaJackut.carregarDados()` lê o arquivo `usuarios.dat`
2. Dados são mantidos em memória durante a execução
3. `SistemaJackut.salvarDados()` é chamado após operações que modificam dados
4. `encerrarSistema()` garante que todos os dados sejam salvos antes do encerramento

### Recuperação
Se o arquivo `usuarios.dat` não existir ou estiver corrompido:
- Um novo arquivo é criado automaticamente na próxima operação que modifique dados

## Fachada 

Métodos públicos disponíveis em `br.ufal.ic.p2.jackut.Facade`:

### Gerenciamento de Sistema
- `zerarSistema()` — Limpa todos os dados em memória
- `encerrarSistema()` — Salva dados e finaliza o sistema
- `abrirSessao(login, senha)` → Retorna `id` (String)

### Gerenciamento de Usuários
- `criarUsuario(login, senha, nome)` — Registra novo usuário
- `getAtributoUsuario(login, atributo)` → Retorna valor do atributo
- `editarPerfil(id, atributo, valor)` — Atualiza informações do usuário
- `removerUsuario(id)` — Remove um usuário do sistema

### Relacionamentos
- `adicionarAmigo(id, amigo)` — Envia convite de amizade
- `ehAmigo(login, amigo)` → Retorna `boolean`
- `getAmigos(login)` → Retorna String no formato `{a,b}`
- `adicionarIdolo(id, idolo)` — Adiciona ídolo
- `ehFa(login, idolo)` → Retorna `boolean`
- `getFas(login)` → Retorna fãs do usuário
- `adicionarPaquera(id, paquera)` — Marca como paquera
- `ehPaquera(id, paquera)` → Retorna `boolean`
- `getPaqueras(id)` → Retorna lista de paqueras
- `adicionarInimigo(id, inimigo)` — Adiciona à lista de inimigos

### Mensagens e Recados
- `enviarRecado(id, destinatario, recado)` — Envia recado privado
- `lerRecado(id)` → Retorna próximo recado não lido
- `enviarMensagem(id, comunidade, mensagem)` — Envia mensagem em comunidade
- `lerMensagem(id)` → Retorna próxima mensagem não lida

### Comunidades
- `criarComunidade(sessao, nome, descricao)` — Cria nova comunidade
- `getDescricaoComunidade(nome)` → Retorna descrição
- `getDonoComunidade(nome)` → Retorna login do criador
- `getMembrosComunidade(nome)` → Retorna membros
- `adicionarComunidade(sessao, nome)` — Adiciona usuário a comunidade
- `getComunidades(idOuLogin)` → Retorna comunidades do usuário

## Mensagens de Erro

As seguintes mensagens são validadas pelos testes:

- `"Usuário não cadastrado."`
- `"Atributo não preenchido."`
- `"Usuário já está adicionado como amigo, esperando aceitação do convite."`
- `"Usuário já está adicionado como amigo."`
- `"Usuário não pode adicionar a si mesmo como amigo."`
- `"Usuário não pode enviar recado para si mesmo."`
- `"Não há recados."`

## Solução de Problemas

### BOM / Caractere Invisível no Início dos Arquivos de Teste
Se o EasyAccept retornar erro `"Unknown command: ﻿# ..."`, é necessário remover o BOM:

1. Abra o arquivo `.txt` no IntelliJ
2. Vá em **File** → **File Properties**
3. Escolha **Remove BOM**
4. Reabra e salve o arquivo (será salvo em UTF-8 sem BOM)

### Arquivo `usuarios.dat` Não É Criado
Verifique se:
- Existe um teste que chama `encerrarSistema()` com sucesso
- O teste anterior não lançou exceção
- O arquivo foi gerado na raiz do projeto (`Jackut/usuarios.dat`)

### Limpando os Dados
Para resetar completamente o sistema:
1. Chame `zerarSistema()`
2. Chame `encerrarSistema()`
3. Delete manualmente o arquivo `usuarios.dat` se necessário


