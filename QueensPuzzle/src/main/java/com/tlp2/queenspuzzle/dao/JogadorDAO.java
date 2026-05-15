package com.tlp2.queenspuzzle.dao;

import com.tlp2.queenspuzzle.model.Jogador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {

    public Jogador salvar(Jogador jogador) {
        String sql = "INSERT INTO jogador (nome, pontuacao_total, nivel_maximo) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPontuacaoTotal());
            stmt.setInt(3, jogador.getNivelMaximo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                jogador.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar jogador: " + e.getMessage());
        }
        return jogador;
    }

    public Jogador buscarPorNome(String nome) {
        String sql = "SELECT * FROM jogador WHERE nome = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Jogador j = new Jogador();
                j.setId(rs.getInt("id"));
                j.setNome(rs.getString("nome"));
                j.setPontuacaoTotal(rs.getInt("pontuacao_total"));
                j.setNivelMaximo(rs.getInt("nivel_maximo"));
                return j;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar jogador: " + e.getMessage());
        }
        return null;
    }

    public void atualizar(Jogador jogador) {
        String sql = "UPDATE jogador SET pontuacao_total = ?, nivel_maximo = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jogador.getPontuacaoTotal());
            stmt.setInt(2, jogador.getNivelMaximo());
            stmt.setInt(3, jogador.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar jogador: " + e.getMessage());
        }
    }

    public List<Jogador> getRanking() {
        List<Jogador> lista = new ArrayList<>();
        String sql = "SELECT * FROM jogador ORDER BY pontuacao_total DESC LIMIT 10";
        try (Connection conn = ConexaoBanco.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jogador j = new Jogador();
                j.setId(rs.getInt("id"));
                j.setNome(rs.getString("nome"));
                j.setPontuacaoTotal(rs.getInt("pontuacao_total"));
                j.setNivelMaximo(rs.getInt("nivel_maximo"));
                lista.add(j);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar ranking: " + e.getMessage());
        }
        return lista;
    }
}
