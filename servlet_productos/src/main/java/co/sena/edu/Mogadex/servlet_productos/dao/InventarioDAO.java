package co.sena.edu.mogadex.servlet_productos.dao;

import co.sena.edu.mogadex.servlet_productos.model.Inventario;
import co.sena.edu.mogadex.servlet_productos.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {
    private static final Logger logger = LoggerFactory.getLogger(InventarioDAO.class);
    
    public List<Inventario> getAllInventarios() {
        List<Inventario> inventarios = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                inventarios.add(mapResultSetToInventario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener todos los inventarios: {}", e.getMessage());
        }
        
        return inventarios;
    }
    
    public Inventario getInventarioById(Long id) {
        String sql = "SELECT * FROM inventario WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventario(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener inventario por ID {}: {}", id, e.getMessage());
        }
        
        return null;
    }
    
    public Inventario createInventario(Inventario inventario) {
        String sql = "INSERT INTO inventario (producto_id, cantidad, stock_minimo, stock_maximo, fecha_ultimo_movimiento, tipo_movimiento, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setLong(1, inventario.getProductoId());
            pstmt.setInt(2, inventario.getCantidad());
            pstmt.setInt(3, inventario.getStockMinimo());
            pstmt.setInt(4, inventario.getStockMaximo());
            pstmt.setTimestamp(5, Timestamp.valueOf(inventario.getFechaUltimoMovimiento()));
            pstmt.setString(6, inventario.getTipoMovimiento());
            pstmt.setString(7, inventario.getUbicacion());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inventario.setId(generatedKeys.getLong(1));
                        logger.info("Inventario creado con ID: {}", inventario.getId());
                        return inventario;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al crear inventario: {}", e.getMessage());
        }
        
        return null;
    }
    
    public boolean actualizarStock(Long id, int cantidad) {
        String sql = "UPDATE inventario SET cantidad = ?, fecha_ultimo_movimiento = NOW(), tipo_movimiento = 'ACTUALIZACION' WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cantidad);
            pstmt.setLong(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar stock del inventario con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    public boolean registrarMovimiento(Long id, int cantidad, String tipo) {
        String sql = "UPDATE inventario SET cantidad = ?, fecha_ultimo_movimiento = NOW(), tipo_movimiento = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cantidad);
            pstmt.setString(2, tipo);
            pstmt.setLong(3, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al registrar movimiento en inventario con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    public boolean updateInventario(Long id, Inventario inventario) {
        String sql = "UPDATE inventario SET producto_id = ?, cantidad = ?, stock_minimo = ?, stock_maximo = ?, fecha_ultimo_movimiento = ?, tipo_movimiento = ?, ubicacion = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, inventario.getProductoId());
            pstmt.setInt(2, inventario.getCantidad());
            pstmt.setInt(3, inventario.getStockMinimo());
            pstmt.setInt(4, inventario.getStockMaximo());
            pstmt.setTimestamp(5, Timestamp.valueOf(inventario.getFechaUltimoMovimiento()));
            pstmt.setString(6, inventario.getTipoMovimiento());
            pstmt.setString(7, inventario.getUbicacion());
            pstmt.setLong(8, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar inventario con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    public boolean deleteInventario(Long id) {
        String sql = "DELETE FROM inventario WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar inventario con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    private Inventario mapResultSetToInventario(ResultSet rs) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.setId(rs.getLong("id"));
        inventario.setProductoId(rs.getLong("producto_id"));
        inventario.setCantidad(rs.getInt("cantidad"));
        inventario.setStockMinimo(rs.getInt("stock_minimo"));
        inventario.setStockMaximo(rs.getInt("stock_maximo"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_ultimo_movimiento");
        if (timestamp != null) {
            inventario.setFechaUltimoMovimiento(timestamp.toLocalDateTime());
        }
        
        inventario.setTipoMovimiento(rs.getString("tipo_movimiento"));
        inventario.setUbicacion(rs.getString("ubicacion"));
        return inventario;
    }
}
