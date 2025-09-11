package br.com.projeto;

import static spark.Spark.*;
import br.com.projeto.dao.ProdutoDAO;
import br.com.projeto.model.Produto;
import java.util.List;

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
                  .append(" - R$").append(p.getPreco()).append("</li>");
            }
            sb.append("</ul><a href='/'>Voltar</a>");
            return sb.toString();
        });
    }
}
