package br.com.projeto;

import java.util.List;

import br.com.projeto.dao.ProdutoDAO;
import br.com.projeto.model.Produto;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class App {
    public static void main(String[] args) {
        ProdutoDAO dao = new ProdutoDAO();

        port(4567); // porta padrão do Spark

        // Página inicial com formulário
        get("/", (req, res) -> {
            return "<h1>Cadastro de Produtos</h1>"
                 + "<form action='/adicionar' method='post'>"
                 + "Nome: <input type='text' name='nome'><br>"
                 + "Preço: <input type='text' name='preco'><br>"
                 + "<input type='submit' value='Cadastrar'>"
                 + "</form>"
                 + "<a href='/listar'>Ver Produtos</a>";
        });

        // Recebe formulário e insere no banco
        post("/adicionar", (req, res) -> {
            String nome = req.queryParams("nome");
            double preco = Double.parseDouble(req.queryParams("preco"));
            dao.inserir(new Produto(nome, preco));
            res.redirect("/"); // volta para página inicial
            return null;
        });

        // Lista produtos cadastrados
        get("/listar", (req, res) -> {
            List<Produto> lista = dao.listar();
            StringBuilder sb = new StringBuilder("<h1>Produtos</h1><ul>");
            for (Produto p : lista) {
                sb.append("<li>").append(p.getId()).append(" - ").append(p.getNome())
                .append(" - R$").append(p.getPreco())
                .append(" <a href='/editar/").append(p.getId()).append("'>Editar</a>")
                .append(" <a href='/excluir/").append(p.getId()).append("'>Excluir</a>")
                .append("</li>");
            }
            sb.append("</ul><a href='/'>Voltar</a>");
            return sb.toString();
        });

        // Excluir produto
        get("/excluir/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            dao.excluir(id);
            res.redirect("/listar");
            return null;
        });

        // Editar produto
        get("/editar/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Produto p = dao.buscarPorId(id);
            if (p != null) {
                return "<h1>Editar Produto</h1>"
                    + "<form action='/editar' method='post'>"
                    + "<input type='hidden' name='id' value='" + p.getId() + "'>"
                    + "Nome: <input type='text' name='nome' value='" + p.getNome() + "'><br>"
                    + "Preço: <input type='text' name='preco' value='" + p.getPreco() + "'><br>"
                    + "<input type='submit' value='Atualizar'>"
                    + "</form>";
            }
            res.redirect("/listar");
            return null;
        });

        // Recebe formulário de edição e atualiza no banco
        post("/editar", (req, res) -> {
            int id = Integer.parseInt(req.queryParams("id"));
            String nome = req.queryParams("nome");
            double preco = Double.parseDouble(req.queryParams("preco"));
            dao.atualizar(new Produto(id, nome, preco));
            res.redirect("/listar");
            return null;
        });
    }
}
