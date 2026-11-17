package Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável por abrir e fechar ligações ao MySQL.
 * Lê as configurações do ficheiro src/db.properties.
 */
public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    // Bloco estático: corre uma vez quando a classe é carregada
    static {
        try {
            Properties props = new Properties();
            // Lê o ficheiro db.properties que está na raiz do src
            props.load(new FileInputStream("src/db.properties"));

            url      = props.getProperty("db.url");
            user     = props.getProperty("db.user");
            password = props.getProperty("db.password");
            driver   = props.getProperty("db.driver");

            // Carrega o driver do MySQL
            Class.forName(driver);

        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro db.properties: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL não encontrado. Verifica se o JAR está no Build Path: " + e.getMessage());
        }
    }

    /**
     * Abre uma nova ligação à base de dados.
     * @return Connection ou null se falhar
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Erro ao ligar à base de dados: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fecha uma ligação de forma segura.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ligação: " + e.getMessage());
            }
        }
    }
}