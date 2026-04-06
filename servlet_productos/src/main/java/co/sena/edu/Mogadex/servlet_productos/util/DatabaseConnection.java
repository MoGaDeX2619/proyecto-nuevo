package co.sena.edu.mogadex.servlet_productos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/productos_nelson";
    private static final String USER = "root";
    private static final String PASSWORD = "cielo";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        try {
            Class.forName(DRIVER);
            logger.info("Driver MySQL cargado exitosamente");
        } catch (ClassNotFoundException e) {
            logger.error("Error al cargar el driver MySQL: {}", e.getMessage());
            throw new RuntimeException("No se puede cargar el driver MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.debug("Conexión a la base de datos establecida");
            return connection;
        } catch (SQLException e) {
            logger.error("Error al establecer conexión con la base de datos: {}", e.getMessage());
            throw e;
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.debug("Conexión cerrada exitosamente");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión: {}", e.getMessage());
            }
        }
    }
}
