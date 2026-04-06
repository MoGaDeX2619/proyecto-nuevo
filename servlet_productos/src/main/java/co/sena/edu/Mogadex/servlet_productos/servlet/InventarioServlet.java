package co.sena.edu.mogadex.servlet_productos.servlet;

import co.sena.edu.mogadex.servlet_productos.dao.InventarioDAO;
import co.sena.edu.mogadex.servlet_productos.model.Inventario;
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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "InventarioServlet", urlPatterns = {"/api/inventario", "/api/inventario/*"})
public class InventarioServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(InventarioServlet.class);
    private InventarioDAO inventarioDAO;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        inventarioDAO = new InventarioDAO();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Para soporte de Java 8 time API
        logger.info("InventarioServlet inicializado");
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
                // GET /api/inventario - Obtener todos los inventarios
                List<Inventario> inventarios = inventarioDAO.getAllInventarios();
                objectMapper.writeValue(out, inventarios);
                
            } else {
                // GET /api/inventario/{id}
                try {
                    Long id = Long.parseLong(pathInfo.substring(1));
                    Inventario inventario = inventarioDAO.getInventarioById(id);
                    
                    if (inventario != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(out, inventario);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Inventario no encontrado\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"ID inválido\"}");
                }
            }
            
        } catch (Exception e) {
            logger.error("Error en doGet de InventarioServlet: {}", e.getMessage());
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
            String pathInfo = request.getPathInfo();
            
            if (pathInfo != null && pathInfo.startsWith("/movimiento/")) {
                // POST /api/inventario/movimiento/{id} - Registrar movimiento
                Long id = Long.parseLong(pathInfo.substring("/movimiento/".length()));
                
                String cantidadStr = request.getParameter("cantidad");
                String tipo = request.getParameter("tipo");
                
                if (cantidadStr == null || tipo == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Parámetros cantidad y tipo son requeridos\"}");
                    return;
                }
                
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    
                    if (inventarioDAO.registrarMovimiento(id, cantidad, tipo)) {
                        Inventario inventario = inventarioDAO.getInventarioById(id);
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(out, inventario);
                        logger.info("Movimiento registrado en inventario {}: cantidad={}, tipo={}", id, cantidad, tipo);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Inventario no encontrado\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Cantidad inválida\"}");
                }
                
            } else {
                // POST /api/inventario - Crear nuevo inventario
                Inventario inventario = objectMapper.readValue(request.getReader(), Inventario.class);
                
                // Validaciones básicas
                if (inventario.getProductoId() == null || inventario.getCantidad() < 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Datos del inventario inválidos\"}");
                    return;
                }
                
                // Establecer valores por defecto
                if (inventario.getFechaUltimoMovimiento() == null) {
                    inventario.setFechaUltimoMovimiento(LocalDateTime.now());
                }
                if (inventario.getTipoMovimiento() == null) {
                    inventario.setTipoMovimiento("CREACION");
                }
                if (inventario.getUbicacion() == null) {
                    inventario.setUbicacion("GENERAL");
                }
                
                Inventario nuevoInventario = inventarioDAO.createInventario(inventario);
                
                if (nuevoInventario != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    objectMapper.writeValue(out, nuevoInventario);
                    logger.info("Inventario creado exitosamente: {}", nuevoInventario.getId());
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"No se pudo crear el inventario\"}");
                }
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error en doPost de InventarioServlet: {}", e.getMessage());
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
            
            if (pathInfo != null && pathInfo.startsWith("/actualizar/")) {
                // PUT /api/inventario/actualizar/{id} - Actualizar stock
                Long id = Long.parseLong(pathInfo.substring("/actualizar/".length()));
                
                String cantidadStr = request.getParameter("cantidad");
                
                if (cantidadStr == null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Parámetro cantidad es requerido\"}");
                    return;
                }
                
                try {
                    int cantidad = Integer.parseInt(cantidadStr);
                    
                    if (inventarioDAO.actualizarStock(id, cantidad)) {
                        Inventario inventario = inventarioDAO.getInventarioById(id);
                        response.setStatus(HttpServletResponse.SC_OK);
                        objectMapper.writeValue(out, inventario);
                        logger.info("Stock actualizado en inventario {}: nueva cantidad={}", id, cantidad);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Inventario no encontrado\"}");
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Cantidad inválida\"}");
                }
                
            } else if (pathInfo != null && !pathInfo.equals("/")) {
                // PUT /api/inventario/{id} - Actualizar inventario completo
                Long id = Long.parseLong(pathInfo.substring(1));
                Inventario inventario = objectMapper.readValue(request.getReader(), Inventario.class);
                
                if (inventarioDAO.updateInventario(id, inventario)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    inventario.setId(id);
                    objectMapper.writeValue(out, inventario);
                    logger.info("Inventario actualizado exitosamente: {}", id);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Inventario no encontrado\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID del inventario requerido\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error en doPut de InventarioServlet: {}", e.getMessage());
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
                // DELETE /api/inventario/{id} - Eliminar inventario
                Long id = Long.parseLong(pathInfo.substring(1));
                
                if (inventarioDAO.deleteInventario(id)) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    logger.info("Inventario eliminado exitosamente: {}", id);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Inventario no encontrado\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"ID del inventario requerido\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID inválido\"}");
        } catch (Exception e) {
            logger.error("Error en doDelete de InventarioServlet: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error interno del servidor\"}");
        }
    }
}
