package com.tlp2.queenspuzzle.dao;

import com.tlp2.queenspuzzle.model.Partida;
import java.sql.*;

/**
 * DAO da Partida: salva o histórico de partidas no banco.
 */
public class PartidaDAO {

    /**
     * Salva uma partida finalizada no banco.
     */
    public void salvar(Partida partida) {
        String sql = "INSERT INTO partida (jogador_id, pontuacao, nivel) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, partida.getJogadorId());
            stmt.setInt(2, partida.getPontuacao());
            stmt.setInt(3, partida.getNivel());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar partida: " + e.getMessage());
        }
    }
}
