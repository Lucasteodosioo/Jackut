package br.ufal.ic;

public class Facade {
    private SistemaJackut sistema;

    public Facade() {
        sistema = new SistemaJackut();
    }

    // Reseta o sistema em memória, removendo todos os usuários carregados.
    public void zerarSistema() {
        sistema.zerarSistema();
    }

    // Cria um novo usuário com login, senha e nome; lança exceção em caso de validação.
    public void criarUsuario(String login, String senha, String nome) throws Exception {
        sistema.criarUsuario(login, senha, nome);
    }

    // Retorna o valor de um atributo do usuário (ex.: nome, descricao). Lança se o usuário ou atributo não existir.
    public String getAtributoUsuario(String login, String atributo) throws Exception {
        return sistema.getAtributoUsuario(login, atributo);
    }

    // Abre uma sessão para o usuário validando login/senha e retorna um sessionId único.
    public String abrirSessao(String login, String senha) throws Exception {
        return sistema.abrirSessao(login, senha);
    }

    // Edita um atributo do perfil do usuário associado ao sessionId fornecido.
    public void editarPerfil(String id, String atributo, String valor) throws Exception {
        sistema.editarPerfil(id, atributo, valor);
    }

    // Encerra o sistema: persiste dados em disco e limpa a memória.
    public void encerrarSistema() {
        sistema.encerrarSistema();
    }
}

