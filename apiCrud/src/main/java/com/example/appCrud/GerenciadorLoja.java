package com.example.appCrud;

import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorLoja {

    String jdbcUrl = "jdbc:mysql://localhost:3306/loja";
    String user = "root";
    String password = "152324@Pe";

    public Connection conexao() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public Produto inserirProduto(String nome, float preco, int codigo) {
        try (Connection connection = conexao();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO produtos (nome, preco, codigo) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, nome);
            preparedStatement.setFloat(2, preco);
            preparedStatement.setInt(3, codigo);
            preparedStatement.executeUpdate();

            return new Produto(nome, preco, codigo);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir produto", e);
        }
    }

    public Produto retornarProduto(int codigoRecebido) {
        Produto produtoEncontrado = null;

        try (Connection connection = conexao();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT nome, preco, codigo FROM produtos WHERE codigo = ?")) {

            preparedStatement.setInt(1, codigoRecebido);

            try (ResultSet resultados = preparedStatement.executeQuery()) {
                if (resultados.next()) {
                    String nomeRecebido = resultados.getString("nome");
                    float precoRecebido = resultados.getFloat("preco");

                    produtoEncontrado = new Produto(nomeRecebido, precoRecebido, codigoRecebido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao encontrar produto.", e);

        }
        return produtoEncontrado;
    }

    public List<Produto> listarProdutos(){
        List<Produto> listaDeProdutos = new ArrayList<>();
        try (Connection connection = conexao();
        Statement statement = connection.createStatement();
        ResultSet resultado = statement.executeQuery("SELECT nome, preco, codigo FROM produtos")){

            while(resultado.next()){

                String nomeProduto = resultado.getString("nome");
                float precoProduto = resultado.getFloat("preco");
                int codigoProduto = resultado.getInt(("codigo"));

                listaDeProdutos.add(new Produto(nomeProduto, precoProduto, codigoProduto));
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar produtos.", e);
        }
        return listaDeProdutos;
    }



    public void deletarProduto(int codigoRecebido) {
        try (Connection connection = conexao();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM produtos WHERE codigo = ?")) {

            preparedStatement.setInt(1, codigoRecebido);
            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 0){
                System.out.println("Nenhum produto correspondente foi encontrado. 0 Tabelas afetadas");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao encontrar produto", e);
        }
    }
    public void updateProduto(int codigoRecebido, String nomeNovo, float precoNovo){
        try(Connection connection = conexao();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE produtos SET nome = ?, preco = ? WHERE codigo = ?")){

            preparedStatement.setString(1, nomeNovo);
            preparedStatement.setFloat(2, precoNovo);
            //preparedStatement.setInt(3, codigoNovo);
            preparedStatement.setInt(3, codigoRecebido);
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Erro ao atualizar o produto", e);
        }

    }
}
