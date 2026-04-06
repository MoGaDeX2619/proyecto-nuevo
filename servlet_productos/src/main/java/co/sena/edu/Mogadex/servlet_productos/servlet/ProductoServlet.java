package co.sena.edu.mogadex.servlet_productos.servlet;

import co.sena.edu.mogadex.servlet_productos.dao.ProductoDAO;
import co.sena.edu.mogadex.servlet_productos.model.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/api/productos", "/api/productos/*"})
public class ProductoServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductoServlet.class);
    private ProductoDAO productoDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        productoDAO = new ProductoDAO();
        objectMapper = new ObjectMapper();
        logger.info("ProductoServlet inicializado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/productos - Obtener todos los productos
                List<Producto> productos = productoDAO.getAllProductos();
                objectMapper.writeValue(out, productos);
                
            } else if (pathInfo.startsWith("/categoria/")) {
                // GET /api/productos/categoria/{categoria}
                String categoria = pathInfo.substring("/categoria/".length());
                List<Producto> productos = productoDAO.getProductosByCategoria(categoria);
                objectMapper.writeValue(out, productos);
                
            } else if (pathInfo.startsWith("/codigo/")) {
                // GET /api/productos/codigo/{codigo}
                String codigo = pathInfo.substring("/codigo/".length());
                Producto producto = productoDAO.getProductoByCodigo(codigo);
                
                if (producto != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(out, producto);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Producto no encontrado\"}");
                }
                
            } else {
                // GET /api/productos/{id}
                try {
                    Long id = Long.parseLong(pathInfo.substring(1));
                    Producto producto = productoDAO.getProductoById(id);
                    
                    if (producto != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(out, producto);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Producto no encontrado\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"ID inválido\"}");
                }
            }
            
        } catch (Exception e) {
            logger.error("Error en doGet de ProductoServlet: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // POST /api/productos - Crear nuevo producto
            Producto producto = objectMapper.readValue(request.getReader(), Producto.class);
            
            // Validaciones básicas
            if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty() ||
                producto.getNombre() == null || producto.getNombre().trim().isEmpty() ||
                producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Datos del producto inválidos\"}");
                return;
            }
            
            // Establecer valores por defecto
            if (producto.getStock() < 0) {
                producto.setStock(0);
            }
            producto.setActivo(true);
            
            Producto nuevoProducto = productoDAO.createProducto(producto);
            
            if (nuevoProducto != null) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(out, nuevoProducto);
                logger.info("Producto creado exitosamente: {}", nuevoProducto.getId());
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"No se pudo crear el producto\"}");
            }
            
        } catch (Exception e) {
            logger.error("Error en doPost de ProductoServlet: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo != null && !pathInfo.equals("/")) {
                // PUT /api/productos/{id} - Actualizar producto
                Long id = Long.parseLong(pathInfo.substring(1));
                Producto producto = objectMapper.readValue(request.getReader(), Producto.class);
                
                if (productoDAO.updateProducto(id, producto)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    producto.setId(id);
                    objectMapper.writeValue(out, producto);
                    logger.info("Producto actualizado exitosamente: {}", id);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Producto no encontrado\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID del producto requerido\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error en doPut de ProductoServlet: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo != null && !pathInfo.equals("/")) {
                // DELETE /api/productos/{id} - Eliminar producto
                Long id = Long.parseLong(pathInfo.substring(1));
                
                if (productoDAO.deleteProducto(id)) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    logger.info("Producto eliminado exitosamente: {}", id);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Producto no encontrado\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID del producto requerido\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error en doDelete de ProductoServlet: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor\"}");
        }
    }
}
