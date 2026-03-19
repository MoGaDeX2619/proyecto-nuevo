package co.sena.edu.Mogadex.api_productos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.sena.edu.Mogadex.api_productos.model.Inventario;
import co.sena.edu.Mogadex.api_productos.repository.InventarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    // 🔹 RF-4.1 Control de stock
    public Inventario actualizarStock(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }

        return inventarioRepository.findById(id)
            .map(inv -> {
                inv.setCantidadActual(inv.getCantidadActual() + cantidad);
                inv.setFechaActualizacion(LocalDateTime.now());
                actualizarEstado(inv);
                return inventarioRepository.save(inv);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado con ID: " + id));
    }

    // 🔹 RF-4.4 Entradas y salidas
    public Inventario registrarMovimiento(Long id, int cantidad, String tipo) {
        if (cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }

        if (!tipo.equalsIgnoreCase("entrada") && !tipo.equalsIgnoreCase("salida")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo debe ser 'entrada' o 'salida'");
        }

        return inventarioRepository.findById(id)
            .map(inv -> {
                if (tipo.equalsIgnoreCase("entrada")) {
                    inv.setCantidadActual(inv.getCantidadActual() + cantidad);
                } else {
                    if (inv.getCantidadActual() < cantidad) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                            "Stock insuficiente. Actual: " + inv.getCantidadActual() + ", Solicitado: " + cantidad);
                    }
                    inv.setCantidadActual(inv.getCantidadActual() - cantidad);
                }

                inv.setFechaActualizacion(LocalDateTime.now());
                actualizarEstado(inv);
                return inventarioRepository.save(inv);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado con ID: " + id));
    }

    // Métodos CRUD adicionales
    public List<Inventario> getAllInventarios() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> getInventarioById(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario createInventario(Inventario inventario) {
        inventario.setFechaActualizacion(LocalDateTime.now());
        actualizarEstado(inventario);
        return inventarioRepository.save(inventario);
    }

    public Inventario updateInventario(Long id, Inventario inventarioDetails) {
        return inventarioRepository.findById(id)
            .map(inv -> {
                inv.setCantidadActual(inventarioDetails.getCantidadActual());
                inv.setCantidadMinima(inventarioDetails.getCantidadMinima());
                inv.setProducto(inventarioDetails.getProducto());
                inv.setFechaActualizacion(LocalDateTime.now());
                actualizarEstado(inv);
                return inventarioRepository.save(inv);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado con ID: " + id));
    }

    public void deleteInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventario no encontrado con ID: " + id);
        }
        inventarioRepository.deleteById(id);
    }

    private void actualizarEstado(Inventario inv) {
        if (inv.getCantidadActual() <= 0) {
            inv.setEstado("SIN STOCK");
        } else if (inv.getCantidadActual() <= inv.getCantidadMinima()) {
            inv.setEstado("BAJO STOCK");
        } else {
            inv.setEstado("DISPONIBLE");
        }
    }
}