package co.sena.edu.Mogadex.api_productos.api_productos.controller;

import co.sena.edu.Mogadex.api_productos.api_productos.model.Producto;
import co.sena.edu.Mogadex.api_productos.api_productos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService service;
    // GET /api/productos → retorna lista JSON
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
    // GET /api/productos/1 → retorna un producto JSON
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}