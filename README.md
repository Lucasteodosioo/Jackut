# Jackut — Projeto (Lógica de Negócio)

Este repositório contém a implementação da lógica de negócio do Jackut (projeto da disciplina). Os testes de aceitação são executados com EasyAccept a partir dos arquivos em `tests/`.


Execute pelo IntelliJ (botão Run)

Local dos artefatos importantes
- Código fonte: `src/br/ufal/ic/`
- Tests: `tests/` 
- Biblioteca: `lib/easyaccept (1).jar`
- Persistência: `usuarios.dat` (gera/usa o diretório do projeto)

Facade (métodos expostos aos testes)
Os métodos públicos disponíveis na `br.ufal.ic.Facade` (resumo):
- zerarSistema()
- criarUsuario(login, senha, nome)
- getAtributoUsuario(login, atributo)
- editarPerfil(id, atributo, valor)
- abrirSessao(login, senha) -> retorna id (String)
- encerrarSistema()
- adicionarAmigo(id, amigo)
- ehAmigo(login, amigo) -> boolean
- getAmigos(login) -> String no formato `{a,b}`
- enviarRecado(id, destinatario, recado)
- lerRecado(id) -> String

Mensagens de erro/strings usadas pelos testes
- "Usuário não cadastrado."
- "Atributo não preenchido."
- "Usuário já está adicionado como amigo, esperando aceitação do convite."
- "Usuário já está adicionado como amigo."
- "Usuário não pode adicionar a si mesmo como amigo."
- "Usuário não pode enviar recado para si mesmo."
- "Não há recados."

Observações e resolução de problemas comuns
- BOM / caractere invisível no início dos arquivos de teste: se o EasyAccept reclamar de "Unknown command: ﻿# ..." remova o BOM salvando o arquivo em UTF-8 *sem BOM*. 

```powershell
# Remove BOM dos testes .txt
Va em file, depois em file properties,  
e escolha REMOVE BOM. 
Depois reabra o arquivo 
e salve para remover o BOM.

```

- Arquivo `usuarios.dat` não criado: verifique se o teste que cria/`encerrarSistema` foi executado e terminou sem exceção.


