package com.tlp2.queenspuzzle.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final String URL = "jdbc:postgresql://localhost:5432/queens_puzzle";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "Sr72723310";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
