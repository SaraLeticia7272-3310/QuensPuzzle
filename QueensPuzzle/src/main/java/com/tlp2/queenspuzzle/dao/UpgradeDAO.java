package com.tlp2.queenspuzzle.dao;

import com.tlp2.queenspuzzle.model.Upgrade;
import java.sql.*;

/**
 *
 * @author saral
 */

public class UpgradeDAO {

    public void salvar(Upgrade upgrade) {
        String sql = "INSERT INTO upgrade (jogador_id, dicas_extras, tempo_bonus, pontos_bonus) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, upgrade.getJogadorId());
            stmt.setInt(2, upgrade.getDicasExtras());
            stmt.setInt(3, upgrade.getTempoBonus());
            stmt.setInt(4, upgrade.getPontosBonus());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao salvar upgrade: " + e.getMessage());
        }
    }

    public Upgrade buscarPorJogador(int jogadorId) {
        String sql = "SELECT * FROM upgrade WHERE jogador_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jogadorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Upgrade u = new Upgrade(jogadorId);
                u.setId(rs.getInt("id"));
                u.setDicasExtras(rs.getInt("dicas_extras"));
                u.setTempoBonus(rs.getInt("tempo_bonus"));
                u.setPontosBonus(rs.getInt("pontos_bonus"));
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar upgrade: " + e.getMessage());
        }
        return new Upgrade(jogadorId);
    }

    public void atualizar(Upgrade upgrade) {
        String sql = "UPDATE upgrade SET dicas_extras = ?, tempo_bonus = ?, pontos_bonus = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, upgrade.getDicasExtras());
            stmt.setInt(2, upgrade.getTempoBonus());
            stmt.setInt(3, upgrade.getPontosBonus());
            stmt.setInt(4, upgrade.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar upgrade: " + e.getMessage());
        }
    }
}