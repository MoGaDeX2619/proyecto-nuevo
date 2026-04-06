package co.sena.edu.mogadex.servlet_productos.dao;

import co.sena.edu.mogadex.servlet_productos.model.Producto;
import co.sena.edu.mogadex.servlet_productos.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductoDAO.class);
    
    public List<Producto> getAllProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener todos los productos: {}", e.getMessage());
        }
        
        return productos;
    }
    
    public Producto getProductoById(Long id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener producto por ID {}: {}", id, e.getMessage());
        }
        
        return null;
    }
    
    public Producto getProductoByCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProducto(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener producto por código {}: {}", codigo, e.getMessage());
        }
        
        return null;
    }
    
    public List<Producto> getProductosByCategoria(String categoria) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria = ? ORDER BY nombre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categoria);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapResultSetToProducto(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener productos por categoría {}: {}", categoria, e.getMessage());
        }
        
        return productos;
    }
    
    public Producto createProducto(Producto producto) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, precio, categoria, stock, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setBigDecimal(4, producto.getPrecio());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setInt(6, producto.getStock());
            pstmt.setBoolean(7, producto.isActivo());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setId(generatedKeys.getLong(1));
                        logger.info("Producto creado con ID: {}", producto.getId());
                        return producto;
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al crear producto: {}", e.getMessage());
        }
        
        return null;
    }
    
    public boolean updateProducto(Long id, Producto producto) {
        String sql = "UPDATE productos SET codigo = ?, nombre = ?, descripcion = ?, precio = ?, categoria = ?, stock = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getCodigo());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getDescripcion());
            pstmt.setBigDecimal(4, producto.getPrecio());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setInt(6, producto.getStock());
            pstmt.setBoolean(7, producto.isActivo());
            pstmt.setLong(8, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar producto con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    public boolean deleteProducto(Long id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar producto con ID {}: {}", id, e.getMessage());
            return false;
        }
    }
    
    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setCodigo(rs.getString("codigo"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setStock(rs.getInt("stock"));
        producto.setActivo(rs.getBoolean("activo"));
        return producto;
    }
}
