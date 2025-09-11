package br.com.projeto.dao;

import br.com.projeto.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private final String URL = "jdbc:postgresql://localhost:5432/TI2-Exerc3";
    private final String USER = "postgres";
    private final String PASS = "ECGJOOAB";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void inserir(Produto p) {
        String sql = "INSERT INTO Produto(nome, preco) VALUES (?, ?)";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Produto";
        try (Connection conn = conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
