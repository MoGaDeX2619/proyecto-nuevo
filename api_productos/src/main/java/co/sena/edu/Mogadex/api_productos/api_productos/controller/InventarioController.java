package co.sena.edu.mogadex.api_productos.api_productos.controller;

import co.sena.edu.mogadex.api_productos.api_productos.model.Inventario;
import co.sena.edu.mogadex.api_productos.api_productos.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
@Validated
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Obtener todos los inventarios
    @GetMapping
    public List<Inventario> getAllInventarios() {
        return inventarioService.getAllInventarios();
    }

    // Obtener inventario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> getInventarioById(@PathVariable Long id) {
        Optional<Inventario> inventario = inventarioService.getInventarioById(id);
        return inventario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo inventario
    @PostMapping
    public ResponseEntity<Inventario> createInventario(@Validated @RequestBody Inventario inventario) {
        try {
            Inventario nuevoInventario = inventarioService.createInventario(inventario);
            return new ResponseEntity<>(nuevoInventario, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // RF-4.1 Actualizar stock
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Inventario> actualizarStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        try {
            Inventario inventario = inventarioService.actualizarStock(id, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // RF-4.4 Registrar movimiento
    @PostMapping("/movimiento/{id}")
    public ResponseEntity<Inventario> registrarMovimiento(
            @PathVariable Long id,
            @RequestParam int cantidad,
            @RequestParam String tipo) {
        try {
            Inventario inventario = inventarioService.registrarMovimiento(id, cantidad, tipo);
            return ResponseEntity.ok(inventario);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar inventario completo
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> updateInventario(@PathVariable Long id, @Validated @RequestBody Inventario inventarioDetails) {
        try {
            Inventario updatedInventario = inventarioService.updateInventario(id, inventarioDetails);
            return ResponseEntity.ok(updatedInventario);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar inventario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        try {
            inventarioService.deleteInventario(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}